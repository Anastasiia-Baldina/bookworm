package org.vse.bookworm.repository;

import org.vse.bookworm.dao.UserBook;

import javax.annotation.Nullable;
import java.util.Collection;

public interface UserBookRepository {

    void create(UserBook userBook);

    void update(UserBook userBook);

    Collection<UserBook> findByUserId(long userId);

    @Nullable
    UserBook get(long userId, String bookId);

    boolean delete(long userId, String bookId);
}
