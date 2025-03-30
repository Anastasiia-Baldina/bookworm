package org.vse.bookworm.dto.internal;

public class LoginResponseDto {
    private long userId;
    private String userName;
    private int loginCode;

    public long getUserId() {
        return userId;
    }

    public LoginResponseDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public LoginResponseDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public int getLoginCode() {
        return loginCode;
    }

    public LoginResponseDto setLoginCode(int loginCode) {
        this.loginCode = loginCode;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", loginCode=" + loginCode +
                '}';
    }
}
