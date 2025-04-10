package org.vse.bookworm.dto.internal;

public class JoinChatRequestDto {
    private long chatId;
    private String chatName;

    public long getChatId() {
        return chatId;
    }

    public JoinChatRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getChatName() {
        return chatName;
    }

    public JoinChatRequestDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }
}
