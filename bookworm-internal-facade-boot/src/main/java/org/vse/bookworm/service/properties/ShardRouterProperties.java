package org.vse.bookworm.service.properties;

import java.util.List;

public class ShardRouterProperties {
    private int partitionCount;
    private List<ShardProperties> shards;

    public int getPartitionCount() {
        return partitionCount;
    }

    public void setPartitionCount(int partitionCount) {
        this.partitionCount = partitionCount;
    }

    public List<ShardProperties> getShards() {
        return shards;
    }

    public void setShards(List<ShardProperties> shards) {
        this.shards = shards;
    }
}
