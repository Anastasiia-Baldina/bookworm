package org.vse.bookworm.dto.internal;

public class MessageSaveResponseDto {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public MessageSaveResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
