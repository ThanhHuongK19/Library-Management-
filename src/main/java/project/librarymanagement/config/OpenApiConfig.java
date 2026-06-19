package project.librarymanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Auto create Swagger UI

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    // @Bean thông báo vs Spring: Run hàm này lấy obj thu đc (OpenAPI)
    // ném vào Spring Container để quản lý
    @Bean
    public OpenAPI libraryManagementOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Library Management API")
                        .description("REST API for Library Management System")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thanh Huong")
                                .email("dh129960@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(SECURITY_SCHEME_NAME)
                )
                .components(new Components()
                        .addSecuritySchemes(
                                SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}