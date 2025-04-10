package ru.vse.bookworm.book;

import androidx.annotation.Nullable;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HeaderType {
    ISBN("isbn"),
    BOOK_NAME("book-name"),
    BOOK_TITLE("book-title"),
    FIRST_NAME("first-name"),
    MIDDLE_NAME("middle-name"),
    LAST_NAME("last-name");

    private static final Map<String, HeaderType> byTag = Stream.of(HeaderType.values())
            .collect(Collectors.toMap(HeaderType::tag, x -> x));
    private final String tag;

    HeaderType(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }

    @Nullable
    public static HeaderType ofTag(String tag) {
        return byTag.get(tag);
    }

    public static boolean containsTag(String tag) {
        return ofTag(tag) != null;
    }
}
