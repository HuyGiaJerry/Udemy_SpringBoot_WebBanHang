package com.project.shopapp.configurations;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(
                title = "ShopApp API Documentation",
                version = "1.0",
                description = "This is the API documentation for the ShopApp application."
        ),
        servers = {
                @Server(url = "http://localhost:8088", description = "Development server"),
//                @Server(url = "https://api.shopapp.com", description = "Production server")
        }
)

@SecurityScheme(
        name = "bearer-key",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)


@Configuration
public class OpenApiConfig {
}
