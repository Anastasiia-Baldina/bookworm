package org.vse.bookworm.dto.internal;

public class UnsubscribeRequestDto {
    private long userId;
    private long chatId;

    public long getUserId() {
        return userId;
    }

    public UnsubscribeRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getChatId() {
        return chatId;
    }

    public UnsubscribeRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }
}
