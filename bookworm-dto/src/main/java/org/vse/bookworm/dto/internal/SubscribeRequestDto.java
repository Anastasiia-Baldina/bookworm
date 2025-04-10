package org.vse.bookworm.dto.internal;

public class SubscribeRequestDto {
    private long userId;
    private long chatId;
    private String chatName;

    public long getUserId() {
        return userId;
    }

    public SubscribeRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getChatId() {
        return chatId;
    }

    public SubscribeRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getChatName() {
        return chatName;
    }

    public SubscribeRequestDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }
}
