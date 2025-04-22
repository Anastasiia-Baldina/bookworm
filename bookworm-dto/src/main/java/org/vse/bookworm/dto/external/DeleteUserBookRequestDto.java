package org.vse.bookworm.dto.external;

public class DeleteUserBookRequestDto {
    private String session;
    private long userId;
    private String bookId;

    public String getSession() {
        return session;
    }

    public DeleteUserBookRequestDto setSession(String session) {
        this.session = session;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public DeleteUserBookRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getBookId() {
        return bookId;
    }

    public DeleteUserBookRequestDto setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }
}
