package com.hotel.roomBooker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/hotels/**", "/rooms/**", "/users/**", "/booking/**")
                .build();
    }

    @Bean
    public OpenAPI roomBookerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RoomBooker API")
                        .description("API documentation for RoomBooker application")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("in telegram: @ben4in")
                                .email("b.bobokulov@outlook.com"))
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .components(new Components()
                        .addSecuritySchemes("basicScheme",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList("basicScheme"));
    }
}
