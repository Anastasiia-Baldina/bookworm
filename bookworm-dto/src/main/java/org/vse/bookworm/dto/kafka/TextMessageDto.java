package org.vse.bookworm.dto.kafka;

import java.util.Date;

public class TextMessageDto implements Partitioned {
    private long messageId;
    private String text;
    private Date date;
    private UserDto sender;
    private ChatDto chat;
    private long affinityKey;
    private boolean joinRequest;
    private boolean edited;

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

    public void setJoinRequest(boolean joinRequest) {
        this.joinRequest = joinRequest;
    }

    public boolean isJoinRequest() {
        return joinRequest;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
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
                ", joined=" + isJoinRequest() +
                '}';
    }

    public static class Builder {
        private long messageId;
        private String text;
        private Date date;
        private UserDto sender;
        private ChatDto chat;
        private long affinityKey;
        private boolean joinRequest;
        private boolean edited;

        public TextMessageDto build() {
            TextMessageDto msg = new TextMessageDto();
            msg.setDate(date);
            msg.setText(text);
            msg.setMessageId(messageId);
            msg.setSender(sender);
            msg.setChat(chat);
            msg.setAffinityKey(affinityKey);
            msg.setJoinRequest(joinRequest);
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

        public Builder setJoinRequest(boolean joinRequest) {
            this.joinRequest = joinRequest;
            return this;
        }

        public Builder setEdited(boolean edited) {
            this.edited = edited;
            return this;
        }
    }
}
