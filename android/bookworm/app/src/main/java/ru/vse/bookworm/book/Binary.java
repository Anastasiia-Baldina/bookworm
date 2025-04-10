package ru.vse.bookworm.book;

import androidx.annotation.NonNull;

public class Binary {
    private final String id;
    private final String contentType;
    private final byte[] data;

    private Binary(Builder b) {
        id = b.id;
        contentType = b.contentType;
        data = b.data;
    }

    @NonNull
    public String id() {
        return id;
    }

    @NonNull
    public String contentType() {
        return contentType;
    }

    @NonNull
    public byte[] data() {
        return data;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private String contentType;
        private byte[] data;

        public Binary build() {
            return new Binary(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }
    }
}
