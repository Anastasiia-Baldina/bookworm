package org.vse.bookworm.dao;

public class Subscriber {
    private final long userId;
    private final long chatId;

    private Subscriber(long userId, long chatId) {
        this.userId = userId;
        this.chatId = chatId;
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

    public static class Builder {
        private long userId;
        private long chatId;

        public Subscriber build() {
            return new Subscriber(userId, chatId);
        }


        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }
    }
}
