package org.vse.bookworm.dto.internal;

public class DeleteMessageRequestDto {
    private long chatId;
    private long messageId;

    public long getChatId() {
        return chatId;
    }

    public DeleteMessageRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public long getMessageId() {
        return messageId;
    }

    public DeleteMessageRequestDto setMessageId(long messageId) {
        this.messageId = messageId;
        return this;
    }
}
