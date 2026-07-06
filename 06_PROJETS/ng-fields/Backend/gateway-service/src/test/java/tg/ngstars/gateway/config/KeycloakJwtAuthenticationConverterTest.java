package tg.ngstars.gateway.config;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

class KeycloakJwtAuthenticationConverterTest {

    final KeycloakJwtAuthenticationConverter converter = new KeycloakJwtAuthenticationConverter();

    @Test
    void shouldExtractRoleFromRealmAccess() {
        var jwt = Jwt.withTokenValue("t")
                .header("alg", "RS256")
                .subject("user-123")
                .claim("realm_access", Map.of("roles", List.of("ADMIN", "MANAGER")))
                .build();

        var auth = converter.convert(jwt).block();

        assertNotNull(auth);
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MANAGER")));
    }

    @Test
    void shouldReturnEmptyAuthoritiesWhenNoRealmAccess() {
        var jwt = Jwt.withTokenValue("t")
                .header("alg", "RS256")
                .subject("user-123")
                .build();

        var auth = converter.convert(jwt).block();
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().isEmpty());
    }

    @Test
    void shouldReturnEmptyAuthoritiesWhenNoRoles() {
        var jwt = Jwt.withTokenValue("t")
                .header("alg", "RS256")
                .subject("user-123")
                .claim("realm_access", Map.of())
                .build();

        var auth = converter.convert(jwt).block();
        assertNotNull(auth);
        assertTrue(auth.getAuthorities().isEmpty());
    }

    @Test
    void shouldKeepSubjectInToken() {
        var jwt = Jwt.withTokenValue("t")
                .header("alg", "RS256")
                .subject("user-456")
                .claim("realm_access", Map.of("roles", List.of("TECHNICIAN")))
                .build();

        var auth = converter.convert(jwt).block();
        assertNotNull(auth);
        assertEquals("user-456", auth.getName());
    }
}
