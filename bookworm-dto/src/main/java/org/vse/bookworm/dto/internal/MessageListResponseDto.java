package org.vse.bookworm.dto.internal;

import java.util.List;

public class MessageListResponseDto {
    private boolean success;
    private List<MessageDto> messages;

    public boolean isSuccess() {
        return success;
    }

    public MessageListResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public List<MessageDto> getMessages() {
        return messages;
    }

    public MessageListResponseDto setMessages(List<MessageDto> messages) {
        this.messages = messages;
        return this;
    }
}
