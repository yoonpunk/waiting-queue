package com.practice.waitingqueue.common.config;

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
            .info(new Info().title("Waiting Queue Scheduler Internal API").version("v1"));
    }

    @Bean
    public GroupedOpenApi internalAdminApi() {
        return GroupedOpenApi.builder()
            .group("waiting-queue-internal-admin-api")
            .packagesToScan("com.practice.waitingqueue.presentation.api")
            .build();
    }
}
