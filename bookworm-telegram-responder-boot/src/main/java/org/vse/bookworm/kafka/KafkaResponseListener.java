package org.vse.bookworm.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.Partitioned;
import org.vse.bookworm.dto.kafka.utils.UDto;
import org.vse.bookworm.kafka.properties.KafkaProperties;
import org.vse.bookworm.telegram.TelegramResponder;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaResponseListener<T extends Partitioned> implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KafkaResponseListener.class);
    private final Consumer<String, String> kafka;
    private final String topic;
    private final ExecutorService scheduler;
    private final AtomicBoolean started = new AtomicBoolean();
    private final TelegramResponder<T> tlgResponder;
    private final Class<T> responseType;

    public KafkaResponseListener(Class<T> responseType, KafkaProperties cfg, TelegramResponder<T> tlgResponder) {
        this.responseType = responseType;
        this.topic = cfg.getTopic();
        this.tlgResponder = tlgResponder;
        Map<String, Object> props = new HashMap<>(cfg.getConsumer());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        this.kafka = new KafkaConsumer<>(props);
        started.set(true);
        scheduler = schedule(cfg.getDelayOnErrorMillis(), cfg.getPollIntervalMillis());
    }

    private ExecutorService schedule(long delayMillis, long pollIntervalMillis) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleWithFixedDelay(
                () -> poolLoop(pollIntervalMillis), delayMillis, delayMillis, TimeUnit.MILLISECONDS);

        return scheduler;
    }

    private void poolLoop(long pollIntervalMillis) {
        try {
            kafka.subscribe(List.of(topic));
            Duration pollTimeout = Duration.ofMillis(pollIntervalMillis);
            while (started.get() && !Thread.currentThread().isInterrupted()) {
                var kafkaRecs = kafka.poll(pollTimeout);
                if (kafkaRecs.isEmpty()) {
                    continue;
                }
                for (var rec : kafkaRecs) {
                    T rsp;
                    String json = rec.value();
                    try {
                        rsp = UDto.deserialize(json, responseType);
                    } catch (Exception e) {
                        log.error("Failed deserialize kafka record. p={},ofs={},msg={}",
                                rec.partition(), rec.offset(), json);
                        continue;
                    }
                    tlgResponder.send(rsp);
                    log.info("Send response: {}", json);
                }
                kafka.commitSync();
            }
        } catch (Exception e) {
            log.error("Error kafka pooling", e);
        }
    }

    @Override
    public void close() throws InterruptedException {
        if (started.compareAndSet(false, true)) {
            scheduler.shutdown();
            if (scheduler.awaitTermination(10_000, TimeUnit.SECONDS)) {
                scheduler.shutdown();
            }
        }
    }
}
