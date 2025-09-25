package com.senibo.e_commerce_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configures the OpenAPI (Swagger) documentation for the E-commerce API.
 * <p>
 * This class uses annotations to define the API's title, version, description,
 * contact information, and server URLs. It also sets up a global security scheme
 * for JWT Bearer authentication, allowing users to authenticate within the Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "E-commerce API",
                version = "v1.0",
                description = "This is an E-commerce API project based on the requirements by " +
                        "roadmap.sh. You can view the project here: https://roadmap" +
                        ".sh/projects/ecommerce-api.",
                contact = @Contact(name = "Senibo Don-Pedro", email = "senibodonpedro@gmail.com")
        ),
        servers = {
                @Server(url = "https://unsnaky-purselike-jolyn.ngrok-free.dev",
                        description = "Ngrok Development Server"),
                @Server(url = "http://localhost:4403", description = "Local Development Server")
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter the JWT token obtained from the login endpoint('no need for bearer')."
)
public class OpenApiConfig {
}
