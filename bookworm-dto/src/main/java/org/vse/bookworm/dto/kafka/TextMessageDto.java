package org.vse.bookworm.dto.kafka;

import java.util.Date;

public class TextMessageDto implements Partitioned {
    private long messageId;
    private String text;
    private Date date;
    private UserDto sender;
    private ChatDto chat;
    private long affinityKey;
    private boolean joined;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserDto getSender() {
        return sender;
    }

    public void setSender(UserDto sender) {
        this.sender = sender;
    }

    public ChatDto getChat() {
        return chat;
    }

    public void setChat(ChatDto chat) {
        this.chat = chat;
    }

    public long getAffinityKey() {
        return affinityKey;
    }

    public void setAffinityKey(long affinityKey) {
        this.affinityKey = affinityKey;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public boolean isJoined() {
        return joined;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "TextMessageDto{" +
                "messageId=" + messageId +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", sender=" + sender +
                ", chat=" + chat +
                ", affinityKey=" + affinityKey +
                ", joined=" + isJoined() +
                '}';
    }

    public static class Builder {
        private long messageId;
        private String text;
        private Date date;
        private UserDto sender;
        private ChatDto chat;
        private long affinityKey;
        private boolean joined;

        public TextMessageDto build() {
            TextMessageDto msg = new TextMessageDto();
            msg.setDate(date);
            msg.setText(text);
            msg.setMessageId(messageId);
            msg.setSender(sender);
            msg.setChat(chat);
            msg.setAffinityKey(affinityKey);
            msg.setJoined(joined);
            return msg;
        }

        public Builder setMessageId(long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setDate(Date date) {
            this.date = date;
            return this;
        }

        public Builder setSender(UserDto sender) {
            this.sender = sender;
            return this;
        }

        public Builder setChat(ChatDto chat) {
            this.chat = chat;
            return this;
        }

        public Builder setAffinityKey(long affinityKey) {
            this.affinityKey = affinityKey;
            return this;
        }

        public Builder setJoined(boolean joined) {
            this.joined = joined;
            return this;
        }
    }
}
