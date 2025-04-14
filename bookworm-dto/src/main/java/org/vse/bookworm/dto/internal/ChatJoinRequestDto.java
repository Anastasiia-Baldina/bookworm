package org.vse.bookworm.dto.internal;

public class ChatJoinRequestDto {
    private long chatId;
    private String chatName;

    public long getChatId() {
        return chatId;
    }

    public ChatJoinRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getChatName() {
        return chatName;
    }

    public ChatJoinRequestDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }
}
