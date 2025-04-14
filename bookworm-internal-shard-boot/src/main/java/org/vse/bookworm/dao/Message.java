package org.vse.bookworm.dao;

public class Message {
    private final String imageBse64;
    private final String text;

    private Message() {
        imageBse64 = null;
        text = null;
    }

    private Message(Builder b) {
        this.imageBse64 = b.imageBse64;
        this.text = b.text;
    }

    public String getImageBse64() {
        return imageBse64;
    }

    public String getText() {
        return text;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String imageBse64;
        private String text;

        public Message build() {
            return new Message(this);
        }

        public Builder setImageBse64(String imageBse64) {
            this.imageBse64 = imageBse64;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }
    }
}


