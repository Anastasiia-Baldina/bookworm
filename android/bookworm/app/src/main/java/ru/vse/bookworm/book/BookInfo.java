package ru.vse.bookworm.book;

import android.os.Build;

import java.time.Instant;

public class BookInfo {
    private final String id;
    private final String title;
    private final String author;
    private final int progress;
    private final Instant updateTime;
    private final String tgGroup;
    private final int version;
    private final int currentChapter;
    private final long chatId;
    private final double position;

    private BookInfo(Builder b) {
        id = b.id;
        title = b.title;
        author = b.author;
        progress = b.progress;
        updateTime = b.updateTime;
        tgGroup = b.tgGroup;
        version = b.version;
        currentChapter = b.currentChapter;
        chatId = b.chatId;
        position = b.position;
    }

    public String id() {
        return id;
    }

    public String author() {
        return author;
    }

    public String title() {
        return title;
    }

    public int progress() {
        return progress;
    }

    public Instant updateTime() {
        return updateTime;
    }

    public String telegramGroup() {
        return tgGroup;
    }

    public int version() {
        return version;
    }

    public int currentChapter() {
        return currentChapter;
    }

    public long chatId() {
        return chatId;
    }

    public double position() {
        return position;
    }

    public static class Builder {
        private String id;
        private String title;
        private String author;
        private int progress;
        private Instant updateTime;
        private String tgGroup;
        private int version;
        private int currentChapter;
        private long chatId;
        private double position = 0;

        public BookInfo build() {
            return new BookInfo(this);
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setProgress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder setUpdateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setUpdateTime(long updateTime) {
            this.updateTime = Instant.ofEpochMilli(updateTime);
            return this;
        }

        public Builder setAuthor(String author) {
            this.author = author;
            return this;
        }

        public Builder setTgGroup(String tgGroup) {
            this.tgGroup = tgGroup;
            return this;
        }

        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder setCurrentChapter(int currentChapter) {
            this.currentChapter = currentChapter;
            return this;
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder setPosition(double position) {
            this.position = position;
            return this;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .setId(this.id)
                .setTitle(this.title)
                .setAuthor(this.author)
                .setProgress(this.progress)
                .setUpdateTime(this.updateTime)
                .setTgGroup(this.tgGroup)
                .setVersion(this.version)
                .setCurrentChapter(this.currentChapter)
                .setChatId(this.chatId)
                .setPosition(this.position);
    }
}
