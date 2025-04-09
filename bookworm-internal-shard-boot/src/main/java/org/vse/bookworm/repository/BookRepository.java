package org.vse.bookworm.repository;

import org.vse.bookworm.dao.Book;

import javax.annotation.Nullable;
import java.util.Collection;

public interface BookRepository {

    void create(Book book);

    void update(Book book);

    Collection<String> listIds(long chatId);

    @Nullable
    Book get(String bookId);

    @Nullable
    Integer getVersion(String bookId);
}
