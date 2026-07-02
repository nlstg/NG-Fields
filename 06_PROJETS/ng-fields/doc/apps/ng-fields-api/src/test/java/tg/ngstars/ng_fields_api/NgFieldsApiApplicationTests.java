package tg.ngstars.ng_fields_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/ng-fields",
    "keycloak.admin-client-secret=test-secret-not-for-ci",
    "spring.security.oauth2.resourceserver.jwt.jwk-set-uri=http://localhost:8080/realms/ng-fields/protocol/openid-connect/certs"
})
class NgFieldsApiApplicationTests {

    @Test
    void contextLoads() {
    }
}
