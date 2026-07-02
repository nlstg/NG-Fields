package tg.ngstars.ng_fields_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ngFieldsOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("NG-Fields API")
                .description("API de gestion des utilisateurs NG-Fields — inscription publique et administration")
                .version("0.0.1"));
    }
}
