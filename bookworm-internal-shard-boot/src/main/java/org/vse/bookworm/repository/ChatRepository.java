package org.vse.bookworm.repository;

import org.jetbrains.annotations.Nullable;
import org.vse.bookworm.dao.Chat;

public interface ChatRepository {
    @Nullable
    Chat get(long chat_id);

    void save(Chat chat);

    @Nullable
    Chat findByName(String chatName);
}
