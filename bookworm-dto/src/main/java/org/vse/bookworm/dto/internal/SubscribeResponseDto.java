package org.vse.bookworm.dto.internal;

public class SubscribeResponseDto {
    private boolean success;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public SubscribeResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public SubscribeResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
