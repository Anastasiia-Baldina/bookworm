package org.vse.bookworm.boot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vse.bookworm.dto.kafka.TextResponse;
import org.vse.bookworm.kafka.KafkaResponseListener;
import org.vse.bookworm.kafka.properties.KafkaProperties;
import org.vse.bookworm.telegram.TelegramTextResponder;

@Configuration
public class ApplicationConfiguration {
    @Value("${token-id}")
    private String tokenId;

    @Bean
    @ConfigurationProperties(prefix = "kafka-text-response")
    KafkaProperties textResponseKafkaProperties() {
        return new KafkaProperties();
    }

    @Bean
    TelegramBot telegramBot() {
        return new TelegramBot(tokenId);
    }

    @Bean
    TelegramTextResponder telegramTextResponder() {
        return new TelegramTextResponder(telegramBot());
    }

    @Bean
    KafkaResponseListener<TextResponse> kafkaTextResponseListener() {
        return new KafkaResponseListener<>(
                TextResponse.class, textResponseKafkaProperties(), telegramTextResponder());
    }
}
