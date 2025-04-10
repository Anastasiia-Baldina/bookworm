package org.vse.bookworm.dto.internal;

public class UnsubscribeResponseDto {
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public UnsubscribeResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
