package org.vse.bookworm.dto.external;

public class SignInResponseDto {
    private String sessionId;
    private boolean success;
    private String message;

    public String getSessionId() {
        return sessionId;
    }

    public SignInResponseDto setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public SignInResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public SignInResponseDto setMessage(String message) {
        this.message = message;
        return this;
    }
}
