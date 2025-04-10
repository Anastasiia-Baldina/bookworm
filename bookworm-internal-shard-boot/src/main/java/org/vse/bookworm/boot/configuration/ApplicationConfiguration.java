package org.vse.bookworm.boot.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.vse.bookworm.cluster.ClusterClient;
import org.vse.bookworm.properties.ClusterProperties;
import org.vse.bookworm.properties.DbProperties;
import org.vse.bookworm.properties.SessionProperties;
import org.vse.bookworm.repository.BookRepository;
import org.vse.bookworm.repository.ChatMessageRepository;
import org.vse.bookworm.repository.SessionRepository;
import org.vse.bookworm.repository.SubscriberRepository;
import org.vse.bookworm.repository.UserBookRepository;
import org.vse.bookworm.repository.postgres.PostgresBookRepository;
import org.vse.bookworm.repository.postgres.PostgresChatMsgRepository;
import org.vse.bookworm.repository.postgres.PostgresSessionRepository;
import org.vse.bookworm.repository.postgres.PostgresSubscriberRepository;
import org.vse.bookworm.repository.postgres.PostgresUserBookRepository;
import org.vse.bookworm.controller.SessionController;
import org.vse.bookworm.service.SessionService;
import org.vse.bookworm.service.impl.ShardSessionService;
import org.vse.bookworm.utils.IdGenerator;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
public class ApplicationConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "cluster")
    ClusterProperties clusterProperties() {
        return new ClusterProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "session")
    SessionProperties sessionProperties() {
        return new SessionProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "db")
    DbProperties dbProperties() {
        return new DbProperties();
    }

    @Bean
    ClusterClient clusterClient() {
        return new ClusterClient(clusterProperties());
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        clusterClient().register();
    }

    @Bean
    DataSource dataSource() {
        try {
            var cfg = dbProperties();
            var dSrc = new HikariDataSource();
            dSrc.setJdbcUrl(cfg.getUrl());
            dSrc.setUsername(cfg.getUsername());
            dSrc.setPassword(cfg.getPassword());
            dSrc.setSchema(cfg.getSchema());

            dSrc.getConnection().isValid(5_000);
            return dSrc;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dataSource());
    }

    @Bean
    SessionRepository sessionRepository() {
        return new PostgresSessionRepository(namedParameterJdbcTemplate());
    }

    @Bean
    UserBookRepository userBookRepository() {
        return new PostgresUserBookRepository(namedParameterJdbcTemplate());
    }

    @Bean
    BookRepository bookRepository() {
        return new PostgresBookRepository(namedParameterJdbcTemplate());
    }

    @Bean
    ChatMessageRepository chatMessageRepository() {
        return new PostgresChatMsgRepository(namedParameterJdbcTemplate());
    }

    @Bean
    SubscriberRepository subscriberRepository() {
        return new PostgresSubscriberRepository(namedParameterJdbcTemplate());
    }

    @Bean
    IdGenerator idGenerator() {
        return IdGenerator.forUuid();
    }

    @Bean
    SessionService sessionService() {
        return new ShardSessionService(sessionProperties(), sessionRepository(), idGenerator());
    }

    @Bean
    SessionController sessionController() {
        return new SessionController(sessionService());
    }
}
