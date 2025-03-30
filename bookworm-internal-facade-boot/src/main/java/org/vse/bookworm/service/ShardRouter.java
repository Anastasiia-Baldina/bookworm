package org.vse.bookworm.service;

import org.jetbrains.annotations.NotNull;
import org.vse.bookworm.service.properties.ShardRouterProperties;
import org.vse.bookworm.utils.Partitioner;
import org.vse.bookworm.utils.Asserts;

import java.util.List;
import java.util.NoSuchElementException;

public class ShardRouter implements Router {
    private final Partitioner afnFunc;
    private final List<Host> hosts;

    public ShardRouter(Partitioner afnFunc, ShardRouterProperties properties) {
        this.afnFunc = afnFunc;
        this.hosts = Asserts.notEmpty(properties.getShards(), "shards").stream()
                .map(x -> new Host(x.getAddress(), x.getPort()))
                .toList();
    }

    @Override
    @NotNull
    public Host route(long id) {
        int shardId = afnFunc.getShardId(afnFunc.getPartition(id));
        if (shardId >= hosts.size()) {
            throw new NoSuchElementException("Shard with id " + shardId + " not found.");
        }
        return hosts.get(shardId);
    }
}
