package org.vse.bookworm.cluster;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryForever;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.internal.HostInfoDto;
import org.vse.bookworm.properties.ClusterProperties;
import org.vse.bookworm.utils.Json;

import java.io.Closeable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class ClusterClient implements Closeable {
    private static final Logger log = LoggerFactory.getLogger(ClusterClient.class);
    private final CuratorFramework curator;
    private final HostInfoDto data;

    public ClusterClient(ClusterProperties cfg) {
        data = createData(cfg);
        curator = CuratorFrameworkFactory.builder()
                .connectString(cfg.getZookeeperConnectionString())
                .threadFactory(
                        new ThreadFactoryBuilder()
                                .setNameFormat("cluster-notificator-%d")
                                .setDaemon(true)
                                .build()
                )
                .retryPolicy(new RetryForever(5_000))
                .build();
        curator.start();
    }

    private HostInfoDto createData(ClusterProperties cfg) {
        try {
            var host = InetAddress.getLocalHost().getHostName();
            int port = cfg.getServicePort();
            var node = "/cluster/" + host + "_" + port;
            return new HostInfoDto()
                    .setHost(host)
                    .setPort(port)
                    .setShardNum(cfg.getShard());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public void register() {
        try {
            var node = "/cluster/" + data.getHost() + "_" + data.getPort();
            var nodeEntry = Json.toJson(data);
            var zkNode = curator.create()
                    .creatingParentContainersIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(node, nodeEntry.getBytes(StandardCharsets.UTF_8));
            log.info("Join cluster: node={}, value={}", node, nodeEntry);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        var node = "/cluster/" + data.getHost() + "_" + data.getPort();
        try {
            curator.delete().forPath(node);
            log.info("Leave cluster: node={}", node);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        curator.close();
    }
}
