package tg.ngstars.ng_fields_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import tg.ngstars.ng_fields_api.config.KeycloakProperties;

@SpringBootApplication
@EnableConfigurationProperties(KeycloakProperties.class)
public class NgFieldsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NgFieldsApiApplication.class, args);
	}

}
