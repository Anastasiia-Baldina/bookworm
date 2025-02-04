package org.vse.bookworm.kafka.properties;

import java.util.Map;

public class KafkaProperties {
    private String topic;
    private long delayOnErrorMillis;
    private long pollIntervalMillis;
    private Map<String, Object> consumer;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Map<String, Object> getConsumer() {
        return consumer;
    }

    public void setConsumer(Map<String, Object> consumer) {
        this.consumer = consumer;
    }

    public long getDelayOnErrorMillis() {
        return delayOnErrorMillis;
    }

    public void setDelayOnErrorMillis(long delayOnErrorMillis) {
        this.delayOnErrorMillis = delayOnErrorMillis;
    }

    public long getPollIntervalMillis() {
        return pollIntervalMillis;
    }

    public void setPollIntervalMillis(long pollIntervalMillis) {
        this.pollIntervalMillis = pollIntervalMillis;
    }
}

