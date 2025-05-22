package com.supinfo.beunreal_api_gateway.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BeUnreal REST API")
                        .description("BeUnreal API for social media application")
                        .license(new License().name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .version("1.0.0"))
                .addServersItem(new io.swagger.v3.oas.models.servers.Server()
                        .url("http://localhost/api")
                        .description("Local Server"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addTagsItem(new Tag().name("Authentication").description("Endpoints for user registration, login, token refresh, and account management"))
                .addTagsItem(new Tag().name("User").description("Endpoints for managing user profiles, searching for users, and handling friend relationships"))
                .addTagsItem(new Tag().name("Media").description("Endpoints for uploading, downloading, and sharing photos and short videos between users"))
                .addTagsItem(new Tag().name("Message").description("Endpoints for direct and group messaging, message history, and real-time communication"));
    }

    @Bean
    public OperationCustomizer globalResponses() {
        return (operation, handlerMethod) -> {
            ApiResponses apiResponses = operation.getResponses() != null ? operation.getResponses() : new ApiResponses();
            apiResponses.addApiResponse("429", new ApiResponse()
                    .description("Too Many Requests")
                    .content(new Content()
                            .addMediaType("text/plain", new MediaType().example("Too many requests. Please try again later."))));
            operation.setResponses(apiResponses);
            return operation;
        };
    }

}