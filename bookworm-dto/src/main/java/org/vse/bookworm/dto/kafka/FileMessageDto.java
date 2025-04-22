package org.vse.bookworm.dto.kafka;

public class FileMessageDto implements Partitioned {
    private long affinityKey;
    private long messageId;
    private String filename;
    private long fileSize;
    private String fileId;
    private String fileUniqueId;
    private String path;
    private String caption;
    private UserDto user;
    private ChatDto chat;
    private boolean generated;
    private String category;

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public ChatDto getChat() {
        return chat;
    }

    public void setChat(ChatDto chat) {
        this.chat = chat;
    }

    @Override
    public long getAffinityKey() {
        return affinityKey;
    }

    public void setAffinityKey(long affinityKey) {
        this.affinityKey = affinityKey;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUniqueId() {
        return fileUniqueId;
    }

    public void setFileUniqueId(String fileUniqueId) {
        this.fileUniqueId = fileUniqueId;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public boolean isGenerated() {
        return generated;
    }

    public void setGenerated(boolean generated) {
        this.generated = generated;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "FileMessage{" +
                "affinityKey=" + affinityKey +
                ", messageId=" + messageId +
                ", filename='" + filename + '\'' +
                ", fileSize=" + fileSize +
                ", fileId='" + fileId + '\'' +
                ", fileUniqueId='" + fileUniqueId + '\'' +
                ", path='" + path + '\'' +
                ", caption='" + caption + '\'' +
                ", user=" + user +
                ", chat=" + chat +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long affinityKey;
        private long messageId;
        private String filename;
        private long fileSize;
        private String fileId;
        private String fileUniqueId;
        private String path;
        private UserDto user;
        private ChatDto chat;
        private String caption;
        private boolean generated;
        private String category;

        public FileMessageDto build() {
            FileMessageDto fm = new FileMessageDto();
            fm.setAffinityKey(affinityKey);
            fm.setMessageId(messageId);
            fm.setFilename(filename);
            fm.setFileSize(fileSize);
            fm.setFileId(fileId);
            fm.setFileUniqueId(fileUniqueId);
            fm.setPath(path);
            fm.setUser(user);
            fm.setChat(chat);
            fm.setCaption(caption);
            fm.setGenerated(generated);
            fm.setCategory(category);

            return fm;
        }

        public Builder setAffinityKey(long affinityKey) {
            this.affinityKey = affinityKey;
            return this;
        }

        public Builder setMessageId(long messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setFilename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder setFileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder setFileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder setFileUniqueId(String fileUniqueId) {
            this.fileUniqueId = fileUniqueId;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Builder setUser(UserDto user) {
            this.user = user;
            return this;
        }

        public Builder setChat(ChatDto chat) {
            this.chat = chat;
            return this;
        }

        public Builder setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        public Builder setGenerated(boolean generated) {
            this.generated = generated;
            return this;
        }

        public Builder setCategory(String category) {
            this.category = category;
            return this;
        }
    }
}
