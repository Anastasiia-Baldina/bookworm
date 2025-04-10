package org.vse.bookworm.dto.internal;

public class JoinChatResponseDto {
    private boolean success;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public JoinChatResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public JoinChatResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
