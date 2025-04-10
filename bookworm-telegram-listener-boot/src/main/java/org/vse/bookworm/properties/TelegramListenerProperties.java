package org.vse.bookworm.properties;

public class TelegramListenerProperties {
    private long delayOnErrorMillis;
    private int maxPollSize;
    private int maxPollTimeSecs;
    private String selfUsername;

    public long getDelayOnErrorMillis() {
        return delayOnErrorMillis;
    }

    public void setDelayOnErrorMillis(long delayOnErrorMillis) {
        this.delayOnErrorMillis = delayOnErrorMillis;
    }

    public int getMaxPollSize() {
        return maxPollSize;
    }

    public void setMaxPollSize(int maxPollSize) {
        this.maxPollSize = maxPollSize;
    }

    public int getMaxPollTimeSecs() {
        return maxPollTimeSecs;
    }

    public void setMaxPollTimeSecs(int maxPollTimeSecs) {
        this.maxPollTimeSecs = maxPollTimeSecs;
    }

    public String getSelfUsername() {
        return selfUsername;
    }

    public void setSelfUsername(String selfUsername) {
        this.selfUsername = selfUsername;
    }
}
