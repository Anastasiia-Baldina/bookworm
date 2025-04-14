package org.vse.bookworm.dto.internal;

import java.util.List;

public class BookSaveResponseDto {
    private boolean success;
    private List<Long> userIds;

    public boolean isSuccess() {
        return success;
    }

    public BookSaveResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public BookSaveResponseDto setUserIds(List<Long> userIds) {
        this.userIds = userIds;
        return this;
    }
}
