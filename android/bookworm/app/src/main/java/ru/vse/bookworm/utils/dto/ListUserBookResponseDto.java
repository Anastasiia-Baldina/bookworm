package ru.vse.bookworm.utils.dto;

import java.util.List;

public class ListUserBookResponseDto {
    private boolean success;
    private String errorMessage;
    private List<UserBookDto> userBooks;

    public boolean isSuccess() {
        return success;
    }

    public ListUserBookResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ListUserBookResponseDto setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public List<UserBookDto> getUserBooks() {
        return userBooks;
    }

    public ListUserBookResponseDto setUserBooks(List<UserBookDto> userBooks) {
        this.userBooks = userBooks;
        return this;
    }
}
