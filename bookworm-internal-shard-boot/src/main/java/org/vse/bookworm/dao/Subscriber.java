package org.vse.bookworm.dao;

public class Subscriber {
    private final long userId;
    private final long chatId;
    private final String chatName;

    private Subscriber(Builder b) {
        this.userId = b.userId;
        this.chatId = b.chatId;
        this.chatName = b.chatName;
    }

    public long getUserId() {
        return userId;
    }

    public long getChatId() {
        return chatId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getChatName() {
        return chatName;
    }

    public static class Builder {
        private long userId;
        private long chatId;
        private String chatName;

        public Subscriber build() {
            return new Subscriber(this);
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder setChatName(String chatName) {
            this.chatName = chatName;
            return this;
        }
    }
}
