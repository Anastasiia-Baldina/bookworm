package org.vse.bookworm.dto.kafka;

public class FileResponseDto implements Partitioned {
    private long affinityKey;
    private long chatId;
    private String base64Entry;
    private String filename;

    @Override
    public long getAffinityKey() {
        return affinityKey;
    }

    public FileResponseDto setAffinityKey(long affinityKey) {
        this.affinityKey = affinityKey;
        return this;
    }

    public long getChatId() {
        return chatId;
    }

    public FileResponseDto setChatId(long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String getBase64Entry() {
        return base64Entry;
    }

    public FileResponseDto setBase64Entry(String base64Entry) {
        this.base64Entry = base64Entry;
        return this;
    }

    public String getFilename() {
        return filename;
    }

    public FileResponseDto setFilename(String filename) {
        this.filename = filename;
        return this;
    }
}
