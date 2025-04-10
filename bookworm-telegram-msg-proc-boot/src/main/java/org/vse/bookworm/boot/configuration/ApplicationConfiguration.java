package org.vse.bookworm.boot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.kafka.FacadeProperties;
import org.vse.bookworm.kafka.KafkaTextMessageListener;
import org.vse.bookworm.kafka.KafkaTextResponseProducer;
import org.vse.bookworm.processor.cmd.CmdSubscribeHandler;
import org.vse.bookworm.properties.KafkaListenerProperties;
import org.vse.bookworm.properties.KafkaProducerProperties;
import org.vse.bookworm.processor.TextMessageProcessor;
import org.vse.bookworm.processor.cmd.CmdLoginHandler;
import org.vse.bookworm.processor.cmd.CmdStartHandler;
import org.vse.bookworm.processor.cmd.CommandHandler;
import org.vse.bookworm.rest.FacadeClient;

import java.util.List;

@Configuration
public class ApplicationConfiguration {
    @Autowired
    private RestTemplateBuilder restTemplateBuilder;
    @Value("${token-id}")
    private String tokenId;

    @Bean
    TelegramBot telegramBot() {
        return new TelegramBot(tokenId);
    }

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
    @ConfigurationProperties(prefix = "facade")
    FacadeProperties facadeProperties() {
        return new FacadeProperties();
    }

    @Bean
    KafkaTextResponseProducer kafkaTextResponseProducer() {
        return new KafkaTextResponseProducer(kafkaResponseProducerProperties());
    }

    @Bean
    public RestTemplate restTemplate() {
        return restTemplateBuilder.build();
    }

    @Bean
    CommandHandler startCommandHandler() {
        return new CmdStartHandler();
    }

    @Bean
    CommandHandler logingCommandHandler() {
        return new CmdLoginHandler(facadeClient());
    }

    @Bean
    FacadeClient facadeClient() {
        return new FacadeClient(facadeProperties(), restTemplate());
    }

    @Bean
    CommandHandler cmdSubscribeHandler() {
        return new CmdSubscribeHandler(facadeClient(), telegramBot());
    }

    @Bean
    TextMessageProcessor textMessageProcessor() {
        return new TextMessageProcessor(
                List.of(
                        startCommandHandler(),
                        logingCommandHandler(),
                        cmdSubscribeHandler()
                ),
                kafkaTextResponseProducer(),
                facadeClient()
        );
    }

    @Bean
    KafkaTextMessageListener kafkaTextMessageListener() {
        return new KafkaTextMessageListener(kafkaListenerProperties(), textMessageProcessor());
    }

}
