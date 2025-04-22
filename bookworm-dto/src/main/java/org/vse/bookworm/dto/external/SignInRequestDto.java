package org.vse.bookworm.dto.external;

public class SignInRequestDto {
    private long userId;
    private int acceptCode;
    private String deviceId;
    private String deviceName;

    public long getUserId() {
        return userId;
    }

    public SignInRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public int getAcceptCode() {
        return acceptCode;
    }

    public SignInRequestDto setAcceptCode(int acceptCode) {
        this.acceptCode = acceptCode;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public SignInRequestDto setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public SignInRequestDto setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }
}
