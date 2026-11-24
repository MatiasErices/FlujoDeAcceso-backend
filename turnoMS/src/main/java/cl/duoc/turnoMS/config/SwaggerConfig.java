package cl.duoc.turnoMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("turnoMS - Flujo de Acceso")
                        .version("1.0")
                        .description("Turnos, conteos por hora y corrección del supervisor"));
    }
}
