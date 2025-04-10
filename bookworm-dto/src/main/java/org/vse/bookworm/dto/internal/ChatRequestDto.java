package org.vse.bookworm.dto.internal;

public class ChatRequestDto {
    private String chatName;

    public String getChatName() {
        return chatName;
    }

    public ChatRequestDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }
}
