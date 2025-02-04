package org.vse.bookworm.utils;

public interface AffinityFunction {
    int getPartition(long affinityKey);

    int getShardId(int partition);

    static AffinityFunction modFunc(int shardCount, int partitionCount) {
        if (shardCount < 0) {
            throw new IllegalArgumentException("Shard count mustn't be less then zero");
        }
        if (partitionCount < 0) {
            throw new IllegalArgumentException("Partition count mustn't be less then zero");
        }
        if (partitionCount < shardCount) {
            throw new IllegalArgumentException("Shard count must be less then partition count");
        }
        return new AffinityFunction() {
            @Override
            public int getPartition(long affinityKey) {
                return (int) (affinityKey % partitionCount);
            }

            @Override
            public int getShardId(int partition) {
                return partition % shardCount;
            }
        };
    }
}
