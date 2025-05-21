package com.supinfo.beunreal_api_gateway.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "beunreal.properties")
public class EnvConfiguration {
    private String allowedOrigins;
    private String mongoDatabase;
    private String mongoUri;
    private String jwtSecretKey;
    private String springKafkaBootstrapServers;
    private String cloudinaryApiKey;
    private String cloudinaryCloudName;
    private String cloudinaryApiSecret;

    public Map<String, String> getCloudinaryConfig() {
        return Map.of(
                "cloud_name", cloudinaryCloudName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinaryApiSecret
        );
    }
}