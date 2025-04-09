package org.vse.bookworm.dao;

import org.vse.bookworm.utils.Asserts;

import java.time.Instant;

public class UserBook {
    private final long userId;
    private final String source;
    private final String bookId;
    private final int currentChapter;
    private final double currentPosition;
    private final Instant updateTime;
    private final int bookVersion;
    private final int progress;

    private UserBook(Builder b) {
        userId = b.userId;
        bookId = Asserts.notEmpty(b.bookId, "bookId");
        currentChapter = b.currentChapter;
        currentPosition = b.currentPosition;
        updateTime = b.updateTime;
        bookVersion = b.bookVersion;
        progress = b.progress;
        source = b.source;
    }

    public static Builder builder() {
        return new Builder();
    }

    public long getUserId() {
        return userId;
    }

    public String getBookId() {
        return bookId;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public double getCurrentPosition() {
        return currentPosition;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public int getBookVersion() {
        return bookVersion;
    }

    public int getProgress() {
        return progress;
    }

    public String getSource() {
        return source;
    }

    public static class Builder {
        private long userId;
        private String source;
        private String bookId;
        private int currentChapter;
        private double currentPosition;
        private Instant updateTime;
        private int bookVersion;
        private int progress;

        public UserBook build() {
            return new UserBook(this);
        }

        public Builder setUserId(long userId) {
            this.userId = userId;
            return this;
        }

        public Builder setBookId(String bookId) {
            this.bookId = bookId;
            return this;
        }

        public Builder setCurrentChapter(int currentChapter) {
            this.currentChapter = currentChapter;
            return this;
        }

        public Builder setCurrentPosition(double currentPosition) {
            this.currentPosition = currentPosition;
            return this;
        }

        public Builder setUpdateTime(Instant updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder setBookVersion(int bookVersion) {
            this.bookVersion = bookVersion;
            return this;
        }

        public Builder setProgress(int progress) {
            this.progress = progress;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }
    }
}
