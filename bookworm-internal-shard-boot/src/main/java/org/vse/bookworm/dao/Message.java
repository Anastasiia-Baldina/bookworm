package org.vse.bookworm.dao;

import org.vse.bookworm.utils.Json;

public class Message {
    private final String imageBse64;
    private final String text;

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

    public String getJson() {
        return Json.toJson(this);
    }

    public static Message fromJson(String json) {
        return Json.fromJson(json, Message.class);
    }

    public static Builder build() {
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


