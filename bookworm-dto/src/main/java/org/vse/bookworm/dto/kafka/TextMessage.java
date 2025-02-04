package org.vse.bookworm.dto.kafka;

import java.util.Date;

public class TextMessage implements Partitioned {
    private long messageId;
    private String text;
    private Date date;
    private User sender;
    private Chat chat;
    private long affinityKey;

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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "TextMessage{" +
                "messageId=" + messageId +
                ", text='" + text + '\'' +
                ", date=" + date +
                ", sender=" + sender +
                ", chat=" + chat +
                '}';
    }

    public long getAffinityKey() {
        return affinityKey;
    }

    public void setAffinityKey(long affinityKey) {
        this.affinityKey = affinityKey;
    }

    public static class Builder {
        private long messageId;
        private String text;
        private Date date;
        private User sender;
        private Chat chat;
        private long affinityKey;

        public TextMessage build() {
            TextMessage msg = new TextMessage();
            msg.setDate(date);
            msg.setText(text);
            msg.setMessageId(messageId);
            msg.setSender(sender);
            msg.setChat(chat);
            msg.setAffinityKey(affinityKey);

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

        public Builder setSender(User sender) {
            this.sender = sender;
            return this;
        }

        public Builder setChat(Chat chat) {
            this.chat = chat;
            return this;
        }

        public Builder setAffinityKey(long affinityKey) {
            this.affinityKey = affinityKey;
            return this;
        }
    }
}
