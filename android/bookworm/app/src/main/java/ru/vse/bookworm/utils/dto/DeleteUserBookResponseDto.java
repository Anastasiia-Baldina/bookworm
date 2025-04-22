package ru.vse.bookworm.utils.dto;

public class DeleteUserBookResponseDto {
    private boolean success;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public DeleteUserBookResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public DeleteUserBookResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }
}
