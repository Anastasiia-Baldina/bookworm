package org.vse.bookworm.dto.internal;

public class ChatJoinResponseDto {
    private boolean success;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public ChatJoinResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ChatJoinResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
