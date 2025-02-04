package org.vse.bookworm.boot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vse.bookworm.kafka.KafkaFileMessageListener;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.kafka.properties.KafkaListenerProperties;
import org.vse.bookworm.kafka.properties.KafkaProducerProperties;
import org.vse.bookworm.proc.FileMessageProcessor;
import org.vse.bookworm.proc.properties.ProcessorProperties;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "kafka-text-response")
    KafkaProducerProperties kafkaResponseProducerProperties() {
        return new KafkaProducerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka-file-message")
    KafkaListenerProperties kafkaListenerProperties() {
        return new KafkaListenerProperties();
    }

    @Bean
    KafkaTextResponseProducer kafkaTextResponseProducer() {
        return new KafkaTextResponseProducer(kafkaResponseProducerProperties());
    }

    @Bean
    @ConfigurationProperties(prefix = "file-processor")
    ProcessorProperties processorProperties() {
        return new ProcessorProperties();
    }

    @Bean
    FileMessageProcessor textMessageProcessor() {
        return new FileMessageProcessor(processorProperties(), kafkaTextResponseProducer());
    }

    @Bean
    KafkaFileMessageListener kafkaTextMessageListener() {
        return new KafkaFileMessageListener(kafkaListenerProperties(), textMessageProcessor());
    }
}
