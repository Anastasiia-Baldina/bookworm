package org.vse.bookworm.dto.internal;

import java.util.List;

public class SubscriberListResponseDto {
    private List<Long> userIds;
    private boolean success;

    public List<Long> getUserIds() {
        return userIds;
    }

    public SubscriberListResponseDto setUserIds(List<Long> userIds) {
        this.userIds = userIds;
        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public SubscriberListResponseDto setSuccess(boolean success) {
        this.success = success;
        return this;
    }
}
