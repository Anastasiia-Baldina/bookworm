package org.vse.bookworm.dto.internal;

public class ChatResponseDto {
    private long chatId;
    private String chatName;
    private boolean success;

    public long getChatId() {
        return chatId;
    }

    public ChatResponseDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getChatName() {
        return chatName;
    }

    public ChatResponseDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ChatResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
