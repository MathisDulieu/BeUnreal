package com.supinfo.beunreal_media_service.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "beunreal.properties")
public class EnvConfiguration {
    private String mongoDatabase;
    private String mongoUri;
    private String springKafkaBootstrapServers;
}