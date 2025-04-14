package org.vse.bookworm.dto.internal;

public class SubscriberListRequestDto {
    private long chatId;

    public long getChatId() {
        return chatId;
    }

    public SubscriberListRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }
}
