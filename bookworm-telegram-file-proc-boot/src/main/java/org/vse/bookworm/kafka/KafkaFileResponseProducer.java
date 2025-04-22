package org.vse.bookworm.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.vse.bookworm.dto.kafka.FileResponseDto;
import org.vse.bookworm.dto.kafka.TextResponseDto;
import org.vse.bookworm.dto.kafka.utils.UDto;
import org.vse.bookworm.properties.KafkaProducerProperties;
import org.vse.bookworm.utils.Asserts;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class KafkaFileResponseProducer {
    private final String topic;
    private final KafkaProducer<String, String> client;

    public KafkaFileResponseProducer(KafkaProducerProperties cfg) {
        this.topic = Asserts.notEmpty(cfg.getTopic(), "topic");
        Map<String, Object> props = new HashMap<>(cfg.getProducer());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        this.client = new KafkaProducer<>(props);
    }

    public Future<RecordMetadata> send(FileResponseDto rsp) {
        var rec = new ProducerRecord<>(
                topic, String.valueOf(rsp.getAffinityKey()), UDto.serialize(rsp));

        return client.send(rec);
    }
}
