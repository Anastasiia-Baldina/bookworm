package org.vse.bookworm.kafka.properties;

import java.util.Map;

public class KafkaProperties {
    private Map<String, String> topics;
    private Map<String, Object> common;
    private Map<String, Object> producer;
    private Map<String, Object> consumer;

    public Map<String, Object> getProducer() {
        return producer;
    }

    public void setProducer(Map<String, Object> producer) {
        this.producer = producer;
    }

    public Map<String, Object> getConsumer() {
        return consumer;
    }

    public void setConsumer(Map<String, Object> consumer) {
        this.consumer = consumer;
    }

    public Map<String, Object> getCommon() {
        return common;
    }

    public void setCommon(Map<String, Object> common) {
        this.common = common;
    }

    public Map<String, String> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, String> topics) {
        this.topics = topics;
    }
}
