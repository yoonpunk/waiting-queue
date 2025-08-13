package com.practice.waitingqueue.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Waiting Queue API").version("v1"));
    }

    @Bean
    public GroupedOpenApi api() {
        return GroupedOpenApi.builder()
            .group("waiting-queue-api")
            .packagesToScan("com.practice.waitingqueue")
            .pathsToMatch("/api/v1/**")
            .build();
    }
}
