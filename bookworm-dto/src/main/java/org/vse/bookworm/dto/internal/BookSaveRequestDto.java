package org.vse.bookworm.dto.internal;

public class BookSaveRequestDto {
    private String bookId;
    private long chatId;
    private String fileBase64;
    private int bookVersion;

    public String getBookId() {
        return bookId;
    }

    public BookSaveRequestDto setBookId(String bookId) {
        this.bookId = bookId;
        return this;
    }

    public long getChatId() {
        return chatId;
    }

    public BookSaveRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getFileBase64() {
        return fileBase64;
    }

    public BookSaveRequestDto setFileBase64(String fileBase64) {
        this.fileBase64 = fileBase64;
        return this;
    }

    public int getBookVersion() {
        return bookVersion;
    }

    public BookSaveRequestDto setBookVersion(int bookVersion) {
        this.bookVersion = bookVersion;
        return this;
    }
}
