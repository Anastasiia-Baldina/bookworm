package org.vse.bookworm.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class Asserts {
    @NotNull
    public static <T> T notNull(@Nullable T param, String paramName) {
        if(param == null) {
            throw new IllegalArgumentException("Parameter ["+ paramName + "] mustn't be null");
        }
        return param;
    }

    @NotNull
    public static <K, T> Map<K, T> notEmpty(Map<K, T> param, String paramName) {
        if(notNull(param, paramName).isEmpty()) {
            throw new IllegalArgumentException("Parameter ["+ paramName + "] mustn't be empty");
        }
        return param;
    }

    public static String notEmpty(String param, String paramName) {
        if(notNull(param, paramName).isEmpty()) {
            throw new IllegalArgumentException("Parameter ["+ paramName + "] mustn't be empty");
        }
        return param;
    }
}
