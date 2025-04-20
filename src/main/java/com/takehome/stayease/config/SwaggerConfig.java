package com.takehome.stayease.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("StayEase API Docs")
                .version("1.0")
                .description("API documentation for StayEase Backend System")
                .contact(new Contact().name("Kumar Sanu").email("support@stayease.com")));
    }
}
