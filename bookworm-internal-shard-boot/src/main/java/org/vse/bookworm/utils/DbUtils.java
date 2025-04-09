package org.vse.bookworm.utils;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.time.Instant;

public final class DbUtils {
    private DbUtils() {
    }

    @Nullable
    public static Timestamp timestamp(@Nullable Instant instant) {
        return instant == null ? null : new Timestamp(instant.toEpochMilli());
    }

    @Nullable
    public static Instant instant(@Nullable Timestamp timestamp) {
        return timestamp == null ? null : Instant.ofEpochMilli(timestamp.getTime());
    }
}
