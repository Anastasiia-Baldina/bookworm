package org.vse.bookworm.repository;

import org.jetbrains.annotations.Nullable;
import org.vse.bookworm.repository.model.User;

public interface UserRepository {

    void create(User user);

    @Nullable
    User get(long id);

    @Nullable
    User get(String username);
}
