package ru.vse.bookworm.book;

public class Header {
    private final HeaderType type;
    private final String text;

    private Header(Builder b) {
        this.type = b.type;
        this.text = b.text;
    }

    public HeaderType type() {
        return type;
    }

    public String text() {
        return text;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HeaderType type;
        private String text;

        public Header build() {
            return new Header(this);
        }


        public Builder setType(HeaderType type) {
            this.type = type;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }
    }
}
