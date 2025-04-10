package org.vse.bookworm.service;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.retry.RetryForever;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.internal.ShardHostDto;
import org.vse.bookworm.properties.ClusterProperties;
import org.vse.bookworm.utils.Json;
import org.vse.bookworm.utils.Partitioner;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ShardRouter implements Router, CuratorCacheListener, Closeable {
    private static final Logger log = LoggerFactory.getLogger(ShardRouter.class);
    private static final String CLUSTER_DIR = "/cluster";
    private final Partitioner afnFunc;
    private final Map<Integer, List<Host>> shards = new ConcurrentHashMap<>();
    private final Map<Integer, AtomicInteger> counters = new HashMap<>();
    private final CuratorFramework curator;
    private final CuratorCache clusterCache;

    public ShardRouter(Partitioner afnFunc, ClusterProperties properties) {
        this.afnFunc = afnFunc;
        for (int i = 0; i < properties.getPartitionCount(); i++) {
            shards.put(i, new ArrayList<>());
            counters.put(i, new AtomicInteger());
        }
        curator = CuratorFrameworkFactory.builder()
                .connectString(properties.getZookeeperConnectionString())
                .threadFactory(
                        new ThreadFactoryBuilder()
                                .setNameFormat("cluster-notificator-%d")
                                .setDaemon(true)
                                .build()
                )
                .retryPolicy(new RetryForever(5_000))
                .build();
        curator.start();
        clusterCache = CuratorCache.build(curator, CLUSTER_DIR);
        clusterCache.listenable()
                .addListener(this);
        clusterCache.start();
    }

    @Override
    @NotNull
    public Host route(Object key) {
        int shardId = afnFunc.getShardId(afnFunc.getPartition(key));
        List<Host> shardHosts = shards.get(shardId);
        if (shardHosts == null) {
            throw new NoSuchElementException("Shard with id " + shardId + " not found.");
        } else if (shardHosts.isEmpty()) {
            throw new IllegalStateException("No hosts for shard=" + shardId);
        }

        int size = shardHosts.size();
        if (size == 1) {
            return shardHosts.getFirst();
        } else {
            int count = Math.abs(counters.get(shardId).incrementAndGet());
            if (count >= size) {
                return shardHosts.get(count % (size - 1));
            } else {
                return shardHosts.get((size - 1) % count);
            }
        }
    }

    @Override
    public void close() {
        clusterCache.close();
        curator.close();
    }

    @Override
    public void event(Type type, ChildData oldData, ChildData data) {
        switch (type) {
            case NODE_CHANGED:
                log.info("Node {} changed", oldData.getPath());
                removeNode(oldData);
                addNode(data);
                break;
            case NODE_DELETED:
                log.info("Node {} deleted", oldData.getPath());
                removeNode(oldData);
                break;
            case NODE_CREATED:
                log.info("Node {} created", data.getPath());
                addNode(data);
                break;
        }
    }

    private void removeNode(ChildData data) {
        var hostInfo = extractData(data);
        if (hostInfo != null) {
            log.info("Remove host {}", hostInfo);
            var shardId = hostInfo.getShardNum();
            var shardHosts = shards.get(shardId);
            if (shardHosts != null) {
                var newHosts = new ArrayList<Host>(shardHosts.size());
                for (var host : shardHosts) {
                    if (Objects.equals(hostInfo.getHost(), host.address()) && hostInfo.getPort() == host.port()) {
                        continue;
                    }
                    newHosts.add(host);
                }
                shards.put(shardId, newHosts);
            }
        }
    }

    private void addNode(ChildData data) {
        var hostInfo = extractData(data);
        if (hostInfo != null) {
            log.info("Add host {}", hostInfo);
            var shardId = hostInfo.getShardNum();
            var shardHosts = shards.get(shardId);
            if (shardHosts != null) {
                var newHosts = new ArrayList<>(shardHosts);
                newHosts.add(new Host(hostInfo.getHost(), hostInfo.getPort()));
                shards.put(shardId, newHosts);
            }
        }
    }

    @Nullable
    private static ShardHostDto extractData(ChildData data) {
        if (data != null
                && data.getPath().startsWith(CLUSTER_DIR + "/")
                && data.getData() != null) {
            return Json.fromJson(new String(data.getData()), ShardHostDto.class);
        }
        return null;
    }
}
