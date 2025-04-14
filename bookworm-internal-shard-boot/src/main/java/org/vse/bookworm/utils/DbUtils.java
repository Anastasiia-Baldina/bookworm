package org.vse.bookworm.utils;

import org.postgresql.util.PGobject;

import javax.annotation.Nullable;
import java.sql.SQLException;
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

    public static PGobject toJsonb(Object obj) {
        if (obj == null) {
            return null;
        }
        PGobject pgObj = new PGobject();
        pgObj.setType("jsonb");
        try {
            pgObj.setValue(Json.toJson(obj));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return pgObj;
    }

    public static <T> T fromJsonb(Object obj, Class<T> type) {
        if (obj instanceof PGobject pgObj) {
            if (pgObj.isNull()) {
                return null;
            }
            return Json.fromJson(pgObj.getValue(), type);
        }
        throw new IllegalArgumentException("Must be PGObject type");
    }
}
