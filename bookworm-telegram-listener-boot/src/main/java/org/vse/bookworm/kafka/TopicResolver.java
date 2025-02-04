package org.vse.bookworm.kafka;

import org.jetbrains.annotations.NotNull;
import org.vse.bookworm.kafka.properties.KafkaProperties;
import org.vse.bookworm.utils.Asserts;

import java.util.Map;

public class TopicResolver {
    private final Map<String, String> typeVsTopic;

    public TopicResolver(KafkaProperties cfg) {
        this.typeVsTopic = Asserts.notEmpty(cfg.getTopics(), "KafkaProperties.topics");
    }

    @NotNull
    public String resolve(@NotNull Class<?> msgType) {
        var topic = typeVsTopic.get(msgType.getSimpleName());
        if (topic == null) {
            throw new IllegalStateException("Unknown message type [" + msgType.getName() + "]");
        }
        return topic;
    }
}
