package org.vse.bookworm.repository.model;

import org.vse.bookworm.utils.Asserts;

import java.time.Instant;

public class Session {
    private final String id;
    private final long userId;
    private final int loginCode;
    private final Instant createTime;
    private final Instant expiredAt;

    public Session(Builder b) {
        id = Asserts.notEmpty(b.id, "id");
        userId = b.userId;
        loginCode = b.loginCode;
        expiredAt = Asserts.notNull(b.expiredAt, "expiredAt");
        createTime = Asserts.notNull(b.createTime, "createTime");
    }

    public String getId() {
        return id;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long userId() {
        return userId;
    }

    public int loginCode() {
        return loginCode;
    }

    public Instant expiredAt() {
        return expiredAt;
    }

    public static class Builder {
        private String id;
        private long userId;
        private int loginCode;
        private Instant createTime;
        private Instant expiredAt;

        public Session build() {
            return new Session(this);
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setLoginCode(int loginCode) {
            this.loginCode = loginCode;
            return this;
        }

        public Builder setExpiredAt(Instant expiredAt) {
            this.expiredAt = expiredAt;
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
    }
}
