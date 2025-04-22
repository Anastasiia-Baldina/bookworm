package org.vse.bookworm.dto.internal;

public class UserBookSaveRequestDto {
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

    public UserBookSaveRequestDto setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public long getUserId() {
        return userId;
    }

    public UserBookSaveRequestDto setUserId(long userId) {
        this.userId = userId;
        return this;
    }

    public long getChatId() {
        return chatId;
    }

    public UserBookSaveRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getChatName() {
        return chatName;
    }

    public UserBookSaveRequestDto setChatName(String chatName) {
        this.chatName = chatName;
        return this;
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public UserBookSaveRequestDto setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
        return this;
    }

    public double getPosition() {
        return position;
    }

    public UserBookSaveRequestDto setPosition(double position) {
        this.position = position;
        return this;
    }

    public int getProgress() {
        return progress;
    }

    public UserBookSaveRequestDto setProgress(int progress) {
        this.progress = progress;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public UserBookSaveRequestDto setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public int getBookVersion() {
        return bookVersion;
    }

    public UserBookSaveRequestDto setBookVersion(int bookVersion) {
        this.bookVersion = bookVersion;
        return this;
    }
}
