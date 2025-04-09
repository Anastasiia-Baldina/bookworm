package org.vse.bookworm.dao;

import org.vse.bookworm.utils.Asserts;

import java.time.Instant;

public class Session {
    private final String id;
    private final long userId;
    private final String username;
    private final String deviceId;
    private final String deviceName;
    private final int acceptCode;
    private final Instant createTime;
    private final Instant updateTime;
    private final int tryCount;

    public Session(Builder b) {
        id = Asserts.notEmpty(b.id, "id");
        userId = b.userId;
        acceptCode = b.acceptCode;
        updateTime = b.updateTime;
        createTime = Asserts.notNull(b.createTime, "createTime");
        deviceId = b.deviceId;
        deviceName = b.deviceName;
        username = b.username;
        tryCount = b.tryCount;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
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

    public int getAcceptCode() {
        return acceptCode;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public String getUsername() {
        return username;
    }

    public int getTryCount() {
        return tryCount;
    }

    public static class Builder {
        private String id;
        private long userId;
        private String username;
        private int acceptCode;
        private Instant createTime;
        private Instant updateTime;
        private String deviceId;
        private String deviceName;
        private int tryCount;

        public Session build() {
            return new Session(this);
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setAcceptCode(int acceptCode) {
            this.acceptCode = acceptCode;
            return this;
        }

        public Builder setUpdateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setCreateTime(Instant createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
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

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setTryCount(int tryCount) {
            this.tryCount = tryCount;
            return this;
        }
    }
}
