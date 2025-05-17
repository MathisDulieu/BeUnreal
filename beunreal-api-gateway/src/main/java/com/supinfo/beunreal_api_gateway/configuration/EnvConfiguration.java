package com.supinfo.beunreal_api_gateway.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "beunreal.properties")
public class EnvConfiguration {
    private String allowedOrigins;
    private String mongoDatabase;
    private String databaseName;
    private String jwtSecretKey;
    private String springKafkaBootstrapServers;
}