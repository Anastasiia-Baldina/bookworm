package ru.vse.bookworm.repository;

import androidx.annotation.Nullable;

import java.util.List;

import ru.vse.bookworm.book.Book;
import ru.vse.bookworm.book.BookInfo;
import ru.vse.bookworm.book.Chapter;

public interface BookRepository {

    void saveBook(Book book);

    @Nullable
    Chapter getChapter(String bookId, int order);

    List<BookInfo> list();

    void markAsDeleted(String id);

    int getCount();
}
