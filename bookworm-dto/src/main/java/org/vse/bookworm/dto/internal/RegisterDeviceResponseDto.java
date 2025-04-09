package org.vse.bookworm.dto.internal;

public class RegisterDeviceResponseDto {
    private boolean success;
    private String sessionId;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public RegisterDeviceResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public RegisterDeviceResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public RegisterDeviceResponseDto setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
}
