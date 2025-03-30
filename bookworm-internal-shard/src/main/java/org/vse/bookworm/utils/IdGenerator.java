package org.vse.bookworm.utils;

import java.util.UUID;

public interface IdGenerator {
    String get();

    static IdGenerator forUuid() {
        return () -> UUID.randomUUID().toString();
    }
}
