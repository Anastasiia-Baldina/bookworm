package org.vse.bookworm.boot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vse.bookworm.kafka.KafkaTextMessageListener;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.kafka.properties.KafkaListenerProperties;
import org.vse.bookworm.kafka.properties.KafkaProducerProperties;
import org.vse.bookworm.processor.TextMessageProcessor;
import org.vse.bookworm.processor.cmd.CmdLoginHandler;
import org.vse.bookworm.processor.cmd.CmdStartHandler;
import org.vse.bookworm.processor.cmd.CommandHandler;

import java.util.List;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "kafka-text-response")
    KafkaProducerProperties kafkaResponseProducerProperties() {
        return new KafkaProducerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka-text-message")
    KafkaListenerProperties kafkaListenerProperties() {
        return new KafkaListenerProperties();
    }

    @Bean
    KafkaTextResponseProducer kafkaTextResponseProducer() {
        return new KafkaTextResponseProducer(kafkaResponseProducerProperties());
    }

    @Bean
    CommandHandler startCommandHandler() {
        return new CmdStartHandler();
    }

    @Bean
    CommandHandler logingCommandHandler() {
        return new CmdLoginHandler();
    }

    @Bean
    TextMessageProcessor textMessageProcessor() {
        return new TextMessageProcessor(
                List.of(
                        startCommandHandler(),
                        logingCommandHandler()
                ),
                kafkaTextResponseProducer()
        );
    }

    @Bean
    KafkaTextMessageListener kafkaTextMessageListener() {
        return new KafkaTextMessageListener(kafkaListenerProperties(), textMessageProcessor());
    }

}
