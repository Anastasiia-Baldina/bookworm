package org.vse.bookworm.utils;

public interface Partitioner {
    int getPartition(Object affinityKey);

    int getShardId(int partition);

    static Partitioner modFunc(int shardCount, int partitionCount) {
        if (shardCount < 0) {
            throw new IllegalArgumentException("Shard count mustn't be less then zero");
        }
        if (partitionCount < 0) {
            throw new IllegalArgumentException("Partition count mustn't be less then zero");
        }
        if (partitionCount < shardCount) {
            throw new IllegalArgumentException("Shard count must be less then partition count");
        }
        return new Partitioner() {
            @Override
            public int getPartition(Object affinityKey) {
                return Math.abs(affinityKey.hashCode() % partitionCount);
            }

            @Override
            public int getShardId(int partition) {
                return partition % shardCount;
            }
        };
    }
}
