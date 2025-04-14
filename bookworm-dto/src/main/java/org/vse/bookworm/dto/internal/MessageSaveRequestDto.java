package org.vse.bookworm.dto.internal;

public class MessageSaveRequestDto {
    private long chatId;
    private long messageId;
    private String category;
    private String message;
    private String username;

    public long getChatId() {
        return chatId;
    }

    public MessageSaveRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public long getMessageId() {
        return messageId;
    }

    public MessageSaveRequestDto setMessageId(long messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public MessageSaveRequestDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public MessageSaveRequestDto setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public MessageSaveRequestDto setUsername(String username) {
        this.username = username;
        return this;
    }
}
