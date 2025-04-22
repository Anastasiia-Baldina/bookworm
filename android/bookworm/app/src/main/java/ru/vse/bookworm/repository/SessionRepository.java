package ru.vse.bookworm.repository;

import androidx.annotation.Nullable;

import ru.vse.bookworm.repository.dao.UserSession;

public interface SessionRepository {
    void save(UserSession session);

    @Nullable
    UserSession get();

    void delete();
}
