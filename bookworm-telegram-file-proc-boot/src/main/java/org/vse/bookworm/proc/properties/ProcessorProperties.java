package org.vse.bookworm.proc.properties;

public class ProcessorProperties {
    private String telegramTokenId;
    private int maxFileSize;

    public String getTelegramTokenId() {
        return telegramTokenId;
    }

    public void setTelegramTokenId(String telegramTokenId) {
        this.telegramTokenId = telegramTokenId;
    }

    public int getMaxFileSize() {
        return maxFileSize;
    }

    public void setMaxFileSize(int maxFileSize) {
        this.maxFileSize = maxFileSize;
    }
}
