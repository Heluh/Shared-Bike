package com.train.campussharedbikeserver.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
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
                        .description("校园共享单车管理系统")
                        .version("v3.0.1")
                        .license(new License().name("Springdoc OpenAPI").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("王宇轩的GitHub")
                        .url("https://github.com/Takagi3-lover"));
    }
}
