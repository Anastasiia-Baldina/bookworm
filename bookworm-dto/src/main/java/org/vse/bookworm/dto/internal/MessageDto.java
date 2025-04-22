package org.vse.bookworm.dto.internal;

public class MessageDto {
    private long messageId;
    private String text;

    public long getMessageId() {
        return messageId;
    }

    public MessageDto setMessageId(long messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getText() {
        return text;
    }

    public MessageDto setText(String text) {
        this.text = text;
        return this;
    }
}
