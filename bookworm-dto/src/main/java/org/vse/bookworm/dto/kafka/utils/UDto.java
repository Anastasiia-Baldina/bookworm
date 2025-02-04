package org.vse.bookworm.dto.kafka.utils;

import com.google.gson.Gson;
import org.vse.bookworm.dto.kafka.TextMessage;

public final class UDto {
    private static final Gson GSON = new Gson();

    private UDto() {
    }

    public static String serialize(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T deserialize(String json, Class<T> dtoType) {
        return GSON.fromJson(json, dtoType);
    }
}
