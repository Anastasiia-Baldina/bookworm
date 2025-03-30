package org.vse.bookworm.repository;

import org.jetbrains.annotations.Nullable;
import org.vse.bookworm.repository.model.Session;

import java.util.Collection;

public interface SessionRepository {
    void create(Session session);

    @Nullable
    Collection<Session> find(long userId);

    @Nullable
    Session get(String sessionId);
}
