package in.coderarmy.gatekeeper.management.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Smart GateKeeper Platform API")
                        .version("1.0.0")
                        .description("Production-grade Multi-Tenant API Gateway Control Plane. " +
                                "Manages Organizations, Multi-Tenant Routes, JWT Authentication, and Kafka Gateway Synchronization.")
                        .contact(new Contact()
                                .name("Mohtashim Khan")
                                .email("manazkhanjhn786@gmail.com")
                                .url("https://github.com/MohtashimKhan007/smart-gatekeeper-platform"))
                        .license(new License().name("Apache 2.0").url("https://spring.io")))
                .servers(List.of(
                        new Server().url("/").description("Default Server (Current Host)")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Enter JWT Bearer token obtained from /api/auth/login")));
    }
}
