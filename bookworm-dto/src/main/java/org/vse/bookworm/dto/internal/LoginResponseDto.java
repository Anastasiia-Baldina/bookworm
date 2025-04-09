package org.vse.bookworm.dto.internal;

public class LoginResponseDto {
    private long userId;

    public long getUserId() {
        return userId;
    }

    public LoginResponseDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "userId=" + userId +
                '}';
    }
}
