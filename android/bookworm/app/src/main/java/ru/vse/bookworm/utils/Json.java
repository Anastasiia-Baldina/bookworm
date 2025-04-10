package ru.vse.bookworm.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

public final class Json {
    private static final Gson GSON = new Gson();

    private Json() {
    }

    @NonNull
    public static String toJson(@NonNull Object obj) {
        return GSON.toJson(obj);
    }

    @Nullable
    public static <T> T fromJson(@Nullable String json, Class<T> type) {
        return json == null ? null : GSON.fromJson(json, type);
    }
}
