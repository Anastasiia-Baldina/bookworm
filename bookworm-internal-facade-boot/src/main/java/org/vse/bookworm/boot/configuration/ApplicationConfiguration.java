package org.vse.bookworm.boot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.vse.bookworm.rest.InternalFacadeController;
import org.vse.bookworm.service.Router;
import org.vse.bookworm.service.ShardRouter;
import org.vse.bookworm.service.properties.ShardRouterProperties;
import org.vse.bookworm.utils.Partitioner;

@Configuration
public class ApplicationConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "router")
    ShardRouterProperties shardRouterProperties() {
        return new ShardRouterProperties();
    }

    @Bean
    Partitioner affinityFunction() {
        var cfg = shardRouterProperties();
        return Partitioner.modFunc(cfg.getPartitionCount(), cfg.getShards().size());
    }

    @Bean
    Router shardRouter() {
        return new ShardRouter(affinityFunction(), shardRouterProperties());
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Do any additional configuration here
        return builder.build();
    }

    @Bean
    InternalFacadeController internalFacadeController(RestTemplate restTemplate) {
        return new InternalFacadeController(shardRouter(), restTemplate);
    }
}
