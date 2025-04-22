package org.vse.bookworm.dto.external;

public class BookResponseDto {
    private boolean success;
    private String errorMessage;
    private String base64Entry;

    public boolean isSuccess() {
        return success;
    }

    public BookResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BookResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getBase64Entry() {
        return base64Entry;
    }

    public BookResponseDto setBase64Entry(String base64Entry) {
        this.base64Entry = base64Entry;
        return this;
    }
}
