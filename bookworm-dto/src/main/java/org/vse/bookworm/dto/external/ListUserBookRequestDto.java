package org.vse.bookworm.dto.external;

public class ListUserBookRequestDto {
    private String session;
    private long userId;

    public String getSession() {
        return session;
    }

    public ListUserBookRequestDto setSession(String session) {
        this.session = session;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public ListUserBookRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }
}
