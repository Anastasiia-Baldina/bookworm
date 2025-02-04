package org.vse.bookworm.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.vse.bookworm.dto.kafka.Partitioned;
import org.vse.bookworm.dto.kafka.utils.UDto;
import org.vse.bookworm.kafka.properties.KafkaProperties;
import org.vse.bookworm.utils.Asserts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class KafkaDataSender {
    private final KafkaProducer<String, String> client;
    private final TopicResolver topicResolver;

    public KafkaDataSender(KafkaProperties cfg, TopicResolver topicResolver) {
        this.topicResolver = topicResolver;
        Map<String, Object> props = new HashMap<>(
                Asserts.notEmpty(cfg.getProducer(), "KafkaProperties.producer")
        );
        if (cfg.getCommon() != null && !cfg.getCommon().isEmpty()) {
            props.putAll(cfg.getCommon());
        }
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        this.client = new KafkaProducer<>(props);
    }

    public <T extends Partitioned> Future<RecordMetadata> send(T msg) {
        var key = msg.getAffinityKey();
        ProducerRecord<String, String> rec = new ProducerRecord<>(
                topicResolver.resolve(msg.getClass()),
                String.valueOf(key),
                UDto.serialize(msg));
        return client.send(rec);
    }
}
