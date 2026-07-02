package tg.ngstars.ng_fields_api.service;

import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tg.ngstars.ng_fields_api.config.KeycloakProperties;
import tg.ngstars.ng_fields_api.dto.CreateUserRequest;
import tg.ngstars.ng_fields_api.dto.UserResponse;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Keycloak keycloakAdmin;
    private final String realm;
    private final AuditService auditService;

    public UserService(Keycloak keycloakAdmin, KeycloakProperties props, AuditService auditService) {
        this.keycloakAdmin = keycloakAdmin;
        this.realm = props.realm();
        this.auditService = auditService;
    }

    public UserResponse createUser(CreateUserRequest request) {
        RealmResource realmResource = keycloakAdmin.realm(realm);

        List<UserRepresentation> existing = realmResource.users().searchByUsername(request.username(), true);
        if (!existing.isEmpty()) {
            throw new IllegalArgumentException("Un utilisateur avec le username '" + request.username() + "' existe déjà");
        }

        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEnabled(true);
        user.setEmailVerified(false);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(false);
        user.setCredentials(List.of(credential));

        Response response = realmResource.users().create(user);
        if (response.getStatus() != 201) {
            String body = response.readEntity(String.class);
            log.error("Keycloak creation failed ({}): {}", response.getStatus(), body);
            throw new RuntimeException("Échec de la création dans Keycloak: " + response.getStatusInfo().getReasonPhrase());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
        log.info("User created in Keycloak: {} (id={})", request.username(), userId);

        RoleRepresentation role = realmResource.roles().get(request.role()).toRepresentation();
        realmResource.users().get(userId).roles().realmLevel().add(List.of(role));
        log.info("Role '{}' assigned to user '{}'", request.role(), request.username());
        auditService.log("CREATE", "User", null, Map.of(
            "username", request.username(),
            "role", request.role(),
            "keycloakId", userId
        ));

        return new UserResponse(userId, request.username(), request.email(),
            request.firstName(), request.lastName(), request.role(), true);
    }

    public List<UserResponse> getUsers() {
        RealmResource realmResource = keycloakAdmin.realm(realm);
        List<UserRepresentation> users = realmResource.users().list();

        return users.stream()
            .flatMap(u -> {
                try {
                    List<RoleRepresentation> roles = realmResource.users()
                        .get(u.getId()).roles().realmLevel().listAll();
                    String primaryRole = roles.stream()
                        .map(RoleRepresentation::getName)
                        .filter(r -> !r.startsWith("default-") && !r.equals("offline_access") && !r.equals("uma_authorization"))
                        .findFirst()
                        .orElse("");
                    return Stream.of(new UserResponse(
                        u.getId(), u.getUsername(), u.getEmail(),
                        u.getFirstName(), u.getLastName(), primaryRole, u.isEnabled()));
                } catch (Exception e) {
                    log.warn("Failed to fetch roles for user {}: {}", u.getUsername(), e.getMessage());
                    return Stream.of(new UserResponse(
                        u.getId(), u.getUsername(), u.getEmail(),
                        u.getFirstName(), u.getLastName(), "", u.isEnabled()));
                }
            })
            .toList();
    }
}
