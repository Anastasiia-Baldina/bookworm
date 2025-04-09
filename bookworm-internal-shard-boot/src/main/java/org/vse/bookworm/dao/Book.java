package org.vse.bookworm.dao;

import org.vse.bookworm.utils.Asserts;

public class Book {
    private final String id;
    private final long chatId;
    private final byte[] file;
    private final int version;

    private Book(Builder b) {
        id = Asserts.notEmpty(b.id, "id");
        chatId = b.chatId;
        file = Asserts.notNull(b.file, "file");
        version = b.version;
    }

    public String getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public byte[] getFile() {
        return file;
    }

    public int getVersion() {
        return version;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private long chatId;
        private byte[] file;
        private int version;

        public Book build() {
            return new Book(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder setFile(byte[] file) {
            this.file = file;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }
    }
}
