package org.vse.bookworm.repository;

import org.jetbrains.annotations.Nullable;
import org.vse.bookworm.dao.Session;

import javax.annotation.Nonnull;
import java.util.Collection;

public interface SessionRepository {

    void create(Session session);

    @Nonnull
    Collection<Session> find(long userId);

    @Nullable
    Session find(long userId, String deviceId);

    @Nullable
    Session get(String sessionId);

    boolean delete(String sessionId);

    boolean attachDevice(Session session);

    boolean refresh(Session session);
}
