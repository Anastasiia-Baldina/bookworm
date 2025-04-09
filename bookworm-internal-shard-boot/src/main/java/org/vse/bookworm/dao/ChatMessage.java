package org.vse.bookworm.dao;

public class ChatMessage {
    private final long chatId;
    private final long messageId;
    private final String category;
    private final Message message;
    private final String username;

    private ChatMessage(Builder b) {
        chatId = b.chatId;
        messageId = b.messageId;
        category = b.category;
        message = b.message;
        username = b.username;
    }

    public long getChatId() {
        return chatId;
    }

    public long getMessageId() {
        return messageId;
    }

    public String getCategory() {
        return category;
    }

    public Message getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long chatId;
        private long messageId;
        private String category;
        private Message message;
        private String username;

        public ChatMessage build() {
            return new ChatMessage(this);
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder setMessageId(long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }

        public Builder setMessage(Message message) {
            this.message = message;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }
    }
}
