package com.gamerate.gamerate.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("GameRate API")
                        .description("Documentación de la API de GameRate para el TFG")
                        .contact(new Contact()
                                .name("Jose Antonio")
                                .email("jacandelapracticas@gmail.com")
                                .url("https://gamerate.com"))
                        .version("1.0")
                );
    }
}