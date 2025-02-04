package org.vse.bookworm.telegram.properties;

public class TelegramListenerProperties {
    private long delayOnErrorMillis;
    private int maxPollSize;
    private int maxPollTimeSecs;

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
}
