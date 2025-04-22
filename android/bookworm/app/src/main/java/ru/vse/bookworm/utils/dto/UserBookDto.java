package ru.vse.bookworm.utils.dto;

public class UserBookDto {
    private String bookId;
    private long userId;
    private long chatId;
    private String chatName;
    private int currentChapter;
    private double position;
    private int progress;
    private long updateTime;
    private int bookVersion;

    public String getBookId() {
        return bookId;
    }

    public UserBookDto setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public UserBookDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getChatId() {
        return chatId;
    }

    public UserBookDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getChatName() {
        return chatName;
    }

    public UserBookDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public UserBookDto setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
        return this;
    }

    public double getPosition() {
        return position;
    }

    public UserBookDto setPosition(double position) {
        this.position = position;
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public UserBookDto setProgress(int progress) {
        this.progress = progress;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public UserBookDto setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public int getBookVersion() {
        return bookVersion;
    }

    public UserBookDto setBookVersion(int bookVersion) {
        this.bookVersion = bookVersion;
        return this;
    }
}
