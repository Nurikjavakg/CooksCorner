package neo.cookscorner.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Collections;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "CooksCorner-Project",
                        email = "nurmukhamedalymbaiuulu064@gmail.com",
                        url = "https://neobis-front-auth-mu.vercel.app"
                ),
                title = "CooksCorner API",
                description = "OpenApi documentation for CooksCorner Project",
                version = "0.0.1"
        ),
        servers = {
                @Server(
                        description = "Railway App",
                        url = "https://cookscorner-production.up.railway.app"
                )
        }
)

public class OpenApiConfig {
    private static final String API_KEY = "Bearer Token";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(API_KEY, apiKeySecuritySchema()))
                .security(Collections.singletonList(new SecurityRequirement().addList(API_KEY)));
    }

    private SecurityScheme apiKeySecuritySchema() {
        return new SecurityScheme()
                .name("Authorization")
                .description("JWT token for authentication")
                .in(SecurityScheme.In.HEADER)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer");
    }
}