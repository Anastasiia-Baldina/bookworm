package org.vse.bookworm.boot.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.controller.InternalFacadeController;
import org.vse.bookworm.properties.ClusterProperties;
import org.vse.bookworm.service.Router;
import org.vse.bookworm.service.ShardRouter;
import org.vse.bookworm.utils.Partitioner;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "cluster")
    ClusterProperties clusterProperties() {
        return new ClusterProperties();
    }

    @Bean
    Partitioner affinityFunction() {
        var cfg = clusterProperties();
        return Partitioner.modFunc(cfg.getPartitionCount(), cfg.getShardCount());
    }

    @Bean
    Router shardRouter() {
        return new ShardRouter(affinityFunction(), clusterProperties());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    InternalFacadeController internalFacadeController(RestTemplate restTemplate) {
        return new InternalFacadeController(shardRouter(), restTemplate);
    }
}
