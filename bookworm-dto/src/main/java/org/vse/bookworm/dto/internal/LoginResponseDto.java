package org.vse.bookworm.dto.internal;

public class LoginResponseDto {
    private long userId;
    private int acceptCode;

    public long getUserId() {
        return userId;
    }

    public LoginResponseDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public int getAcceptCode() {
        return acceptCode;
    }

    public LoginResponseDto setAcceptCode(int acceptCode) {
        this.acceptCode = acceptCode;
        return this;
    }
}
