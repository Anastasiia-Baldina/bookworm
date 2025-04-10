package ru.vse.bookworm.book;

public class Note {
    private final String id;
    private final String text;

    private Note(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String id() {
        return id;
    }

    public String text() {
        return text;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private StringBuilder text = new StringBuilder();

        public Note build() {
            return new Note(id, text.toString());
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder append(String text) {
            this.text.append(text);
            return this;
        }
    }
}
