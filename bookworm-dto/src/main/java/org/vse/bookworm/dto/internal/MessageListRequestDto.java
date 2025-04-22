package org.vse.bookworm.dto.internal;

public class MessageListRequestDto {
    private long chatId;
    private String category;
    private long startMessageId;
    private int limit;

    public long getChatId() {
        return chatId;
    }

    public MessageListRequestDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public MessageListRequestDto setCategory(String category) {
        this.category = category;
        return this;
    }

    public long getStartMessageId() {
        return startMessageId;
    }

    public MessageListRequestDto setStartMessageId(long startMessageId) {
        this.startMessageId = startMessageId;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public MessageListRequestDto setLimit(int limit) {
        this.limit = limit;
        return this;
    }
}
