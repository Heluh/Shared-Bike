package com.train.campussharedbikeserverfence.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("学期实训API设计文档")
                        .description("这是一段描述")
                        .version("v0.0.1")
                        .license(new License().name("Springdoc OpenAPI").url("http://springdoc.org")));
    }
}
