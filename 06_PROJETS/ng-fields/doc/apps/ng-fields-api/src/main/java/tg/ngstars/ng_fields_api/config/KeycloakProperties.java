package tg.ngstars.ng_fields_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
    String authServerUrl,
    String realm,
    String adminClientId,
    String adminClientSecret
) {}
