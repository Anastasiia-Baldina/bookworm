package org.vse.bookworm.dto.internal;

public class UserBookSaveResponseDto {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public UserBookSaveResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
