package ru.vse.bookworm.repository.dao;

import java.time.Instant;

import ru.vse.bookworm.utils.Asserts;

public class UserSession {
    private final String sessionId;
    private final Instant updateTime;
    private final long userId;
    private final String deviceId;
    private final String deviceName;

    private UserSession(Builder b) {
        sessionId = Asserts.notEmpty(b.sessionId, "sessionId");
        updateTime = Asserts.notNull(b.updateTime, "updateTime");
        userId = b.userId;
        deviceId = b.deviceId;
        deviceName = b.deviceName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public long getUserId() {
        return userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public static class Builder {
        private String sessionId;
        private Instant updateTime;
        private long userId;
        private String deviceId;
        private String deviceName;

        public UserSession build() {
            return new UserSession(this);
        }

        public Builder setSessionId(String sessionId) {
            this.sessionId = sessionId;
            return this;
        }

        public Builder setUpdateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setDeviceId(String deviceId) {
            this.deviceId = deviceId;
            return this;
        }

        public Builder setDeviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
