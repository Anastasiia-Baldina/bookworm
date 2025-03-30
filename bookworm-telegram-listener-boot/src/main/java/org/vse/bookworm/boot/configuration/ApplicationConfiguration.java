package org.vse.bookworm.boot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vse.bookworm.kafka.KafkaDataSender;
import org.vse.bookworm.kafka.TopicResolver;
import org.vse.bookworm.kafka.properties.KafkaProperties;
import org.vse.bookworm.telegram.TelegramBotListener;
import org.vse.bookworm.telegram.properties.TelegramListenerProperties;

@Configuration
public class ApplicationConfiguration {
    @Value("${token-id}")
    private String tokenId;

    @Bean
    TelegramBot telegramBot() {
        return new TelegramBot(tokenId);
    }

    @Bean
    @ConfigurationProperties(prefix = "telegram-listener")
    TelegramListenerProperties telegramListenerProperties() {
        return new TelegramListenerProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "kafka")
    KafkaProperties kafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    TopicResolver topicResolver() {
        return new TopicResolver(kafkaProperties());
    }

    @Bean
    KafkaDataSender kafkaDataSender() {
        return new KafkaDataSender(kafkaProperties(), topicResolver());
    }

    @Bean
    TelegramBotListener telegramBotListener() {
        return new TelegramBotListener(
                telegramBot(),
                telegramListenerProperties(),
                kafkaDataSender());
    }
}
