package org.vse.bookworm.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vse.bookworm.dto.kafka.TextMessageDto;
import org.vse.bookworm.dto.kafka.utils.UDto;
import org.vse.bookworm.properties.KafkaListenerProperties;
import org.vse.bookworm.processor.TextMessageProcessor;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class KafkaTextMessageListener implements AutoCloseable {
    private static final Logger log = LoggerFactory.getLogger(KafkaTextMessageListener.class);
    private final Consumer<String, String> kafka;
    private final String topic;
    private final ExecutorService scheduler;
    private final AtomicBoolean started = new AtomicBoolean();
    private final TextMessageProcessor msgProc;

    public KafkaTextMessageListener(KafkaListenerProperties cfg, TextMessageProcessor msgProc) {
        this.topic = cfg.getTopic();
        this.msgProc = msgProc;
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
                List<TextMessageDto> msgList = new ArrayList<>(kafkaRecs.count());
                for (var rec : kafkaRecs) {
                    TextMessageDto msg;
                    String json = rec.value();
                    try {
                        log.info("Kafka fetch: {}", json);
                        msg = UDto.deserialize(json, TextMessageDto.class);
                    } catch (Exception e) {
                        log.error("Failed deserialize kafka record. p={},ofs={},msg={}",
                                rec.partition(), rec.offset(), json);
                        continue;
                    }
                    msgList.add(msg);
                }
                msgProc.process(msgList);
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
