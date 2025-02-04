package org.vse.bookworm.dto.kafka;

public class TextResponse implements Partitioned {
    private long affinityKey;
    private long chatId;
    private String text;

    @Override
    public long getAffinityKey() {
        return affinityKey;
    }

    public void setAffinityKey(long affinityKey) {
        this.affinityKey = affinityKey;
    }

    public long getChatId() {
        return chatId;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextResponse{" +
                "affinityKey=" + affinityKey +
                ", chatId=" + chatId +
                ", text='" + text + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long affinityKey;
        private long chatId;
        private String text;

        public TextResponse build() {
            var rsp = new TextResponse();
            rsp.setAffinityKey(affinityKey);
            rsp.setChatId(chatId);
            rsp.setText(text);

            return rsp;
        }

        public Builder setAffinityKey(long affinityKey) {
            this.affinityKey = affinityKey;
            return this;
        }

        public Builder setChatId(long chatId) {
            this.chatId = chatId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }
    }
}
