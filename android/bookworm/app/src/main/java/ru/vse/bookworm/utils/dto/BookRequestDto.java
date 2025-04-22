package ru.vse.bookworm.utils.dto;

public class BookRequestDto {
    private String session;
    private long userId;
    private String bookId;

    public String getSession() {
        return session;
    }

    public BookRequestDto setSession(String session) {
        this.session = session;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public BookRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getBookId() {
        return bookId;
    }

    public BookRequestDto setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }
}
