package org.vse.bookworm.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vse.bookworm.kafka.KafkaFileMessageListener;
import org.vse.bookworm.kafka.KafkaFileResponseProducer;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.properties.FacadeProperties;
import org.vse.bookworm.properties.KafkaListenerProperties;
import org.vse.bookworm.properties.KafkaProducerProperties;
import org.vse.bookworm.proc.FileMessageProcessor;
import org.vse.bookworm.properties.ProcessorProperties;
import org.vse.bookworm.rest.FacadeClient;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;

    @Bean
    @ConfigurationProperties(prefix = "kafka-text-response")
    KafkaProducerProperties kafkaResponseProducerProperties() {
        return new KafkaProducerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka-file-response")
    KafkaProducerProperties kafkaFileProducerProperties() {
        return new KafkaProducerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka-file-message")
    KafkaListenerProperties kafkaListenerProperties() {
        return new KafkaListenerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "facade")
    FacadeProperties facadeProperties() {
        return new FacadeProperties();
    }

    @Bean
    KafkaTextResponseProducer kafkaTextResponseProducer() {
        return new KafkaTextResponseProducer(kafkaResponseProducerProperties());
    }

    @Bean
    KafkaFileResponseProducer kafkaFileResponseProducer() {
        return new KafkaFileResponseProducer(kafkaFileProducerProperties());
    }

    @Bean
    @ConfigurationProperties(prefix = "file-processor")
    ProcessorProperties processorProperties() {
        return new ProcessorProperties();
    }


    @Bean
    FacadeClient facadeClient() {
        return new FacadeClient(facadeProperties(), restTemplateBuilder.build());
    }

    @Bean
    FileMessageProcessor textMessageProcessor() {
        return new FileMessageProcessor(
                processorProperties(),
                kafkaTextResponseProducer(),
                kafkaFileResponseProducer(),
                facadeClient());
    }

    @Bean
    KafkaFileMessageListener kafkaTextMessageListener() {
        return new KafkaFileMessageListener(kafkaListenerProperties(), textMessageProcessor());
    }
}
