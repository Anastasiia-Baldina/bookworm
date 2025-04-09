package org.vse.bookworm.repository;

import org.vse.bookworm.dao.ChatMessage;

import java.util.Collection;

public interface ChatMessageRepository {

    void insert(ChatMessage chatMsg);

    void delete(long chatId, long messageId);

    Collection<ChatMessage> list(long chatId, String category);
}
