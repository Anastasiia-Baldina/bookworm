package org.vse.bookworm.dto.internal;

public class RegisterDeviceRequestDto {
    private long userId;
    private int acceptCode;
    private String deviceId;
    private String deviceName;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getAcceptCode() {
        return acceptCode;
    }

    public RegisterDeviceRequestDto setAcceptCode(int acceptCode) {
        this.acceptCode = acceptCode;
        return this;
    }
}
