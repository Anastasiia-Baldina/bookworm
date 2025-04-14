package org.vse.bookworm.dto.internal;

public class DeleteMessageResponseDto {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public DeleteMessageResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
