package ru.vse.bookworm.book;

import java.io.InputStream;

public interface Parser {
    Book.Builder parse(InputStream inputStream);
}
