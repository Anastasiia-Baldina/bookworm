package org.vse.bookworm.dto.kafka;

public interface Partitioned {
    long getAffinityKey();
}
