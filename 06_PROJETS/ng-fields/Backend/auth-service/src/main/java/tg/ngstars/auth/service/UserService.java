package tg.ngstars.auth.service;

import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tg.ngstars.auth.config.KeycloakProperties;
import tg.ngstars.auth.dto.CreateUserRequest;
import tg.ngstars.auth.dto.UpdateProfileRequest;
import tg.ngstars.auth.dto.UserResponse;
import tg.ngstars.auth.exception.ConflictException;
import tg.ngstars.auth.exception.NotFoundException;
import tg.ngstars.auth.model.User;
import tg.ngstars.auth.repository.UserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final AuditService auditService;
    private final Keycloak keycloak;
    private final KeycloakProperties keycloakProperties;

    public UserService(UserRepository userRepository, AuditService auditService,
            Keycloak keycloak, KeycloakProperties keycloakProperties) {
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request, String createdBy) {
        if (userRepository.existsByUsername(request.username()))
            throw new ConflictException("Username '" + request.username() + "' deja utilise");
        if (userRepository.existsByEmail(request.email()))
            throw new ConflictException("Email '" + request.email() + "' deja utilise");

        var kcUser = new UserRepresentation();
        kcUser.setUsername(request.username());
        kcUser.setEmail(request.email());
        kcUser.setFirstName(request.firstName());
        kcUser.setLastName(request.lastName());
        kcUser.setEnabled(true);
        if (request.password() != null)
            kcUser.setCredentials(List.of(passwordCredential(request.password())));

        var realm = keycloak.realm(keycloakProperties.realm());
        try (Response response = realm.users().create(kcUser)) {
            if (response.getStatus() != 201)
                throw new RuntimeException("Echec creation Keycloak: " + response.getStatus());

            var location = response.getLocation();
            var keycloakId = UUID.fromString(location.getPath().substring(location.getPath().lastIndexOf('/') + 1));

            if (request.role() != null)
                assignRealmRole(keycloakId.toString(), request.role());

            var user = new User();
            user.setKeycloakId(keycloakId);
            user.setUsername(request.username());
            user.setEmail(request.email());
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user.setRole(request.role());
            user.setPhone(request.phone());
            user.setActive(true);
            userRepository.save(user);

            auditService.log(null, "USER_CREATED", "User", user.getId().toString(),
                    "Compte cree: " + request.username(), createdBy);
            log.info("Compte cree: {} (keycloakId={})", request.username(), keycloakId);

            return toResponse(user);
        }
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getUser(UUID id) {
        return toResponse(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + id)));
    }

    @Transactional
    public UserResponse updateUser(UUID id, CreateUserRequest request, String updatedBy) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + id));
        var kcIdStr = user.getKeycloakId().toString();
        var kcUser = keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).toRepresentation();
        kcUser.setUsername(request.username());
        kcUser.setEmail(request.email());
        kcUser.setFirstName(request.firstName());
        kcUser.setLastName(request.lastName());
        keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).update(kcUser);

        if (request.password() != null)
            keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr)
                    .resetPassword(passwordCredential(request.password()));
        if (request.role() != null && !request.role().equals(user.getRole()))
            assignRealmRole(kcIdStr, request.role());

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRole(request.role());
        user.setPhone(request.phone());
        userRepository.save(user);

        auditService.log(null, "USER_UPDATED", "User", user.getId().toString(),
                "Compte mis a jour: " + user.getUsername(), updatedBy);

        return toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID id, String deletedBy) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + id));
        var kcIdStr = user.getKeycloakId().toString();
        var kcUser = keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).toRepresentation();
        kcUser.setEnabled(false);
        keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).update(kcUser);
        user.setActive(false);
        userRepository.save(user);
        auditService.log(null, "USER_DELETED", "User",
                user.getId().toString(), "Compte desactive: " + user.getEmail(), deletedBy);
    }

    @Transactional
    public UserResponse assignRole(UUID keycloakId, String newRole, String adminId) {
        var user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + keycloakId));
        var kcIdStr = keycloakId.toString();
        var realm = keycloak.realm(keycloakProperties.realm());

        var metierRoles = List.of("ADMIN", "MANAGER", "TECHNICIAN", "CLIENT_PORTAL");
        var toRemove = realm.users().get(kcIdStr).roles().realmLevel().listAll().stream()
                .filter(r -> metierRoles.contains(r.getName()))
                .toList();
        if (!toRemove.isEmpty())
            realm.users().get(kcIdStr).roles().realmLevel().remove(toRemove);

        var role = realm.roles().get(newRole).toRepresentation();
        realm.users().get(kcIdStr).roles().realmLevel().add(List.of(role));

        user.setRole(newRole);
        userRepository.save(user);

        auditService.log(null, "ROLE_ASSIGNED", "User",
                user.getId().toString(), "Role " + newRole + " assigne a " + user.getUsername(), adminId);

        return toResponse(user);
    }

    @Transactional
    public UserResponse updateUserStatus(UUID keycloakId, boolean enabled, String adminId) {
        var user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + keycloakId));
        var kcIdStr = keycloakId.toString();
        var kcUser = keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).toRepresentation();
        kcUser.setEnabled(enabled);
        keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).update(kcUser);

        user.setActive(enabled);
        userRepository.save(user);

        var action = enabled ? "ACCOUNT_ENABLED" : "ACCOUNT_DISABLED";
        auditService.log(null, action, "User",
                user.getId().toString(), "Compte " + user.getUsername() + ": " + (enabled ? "active" : "desactive"), adminId);

        return toResponse(user);
    }

    @Transactional
    public void sendPasswordReset(UUID keycloakId, String adminId) {
        userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable: " + keycloakId));
        keycloak.realm(keycloakProperties.realm()).users().get(keycloakId.toString())
                .executeActionsEmail(List.of("UPDATE_PASSWORD"));
        auditService.log(null, "PASSWORD_RESET_SENT", "User",
                keycloakId.toString(), "Email de reinitialisation envoye", adminId);
        log.info("Email de reinitialisation envoye pour keycloakId={}", keycloakId);
    }

    public UserResponse getProfile(UUID keycloakId) {
        return toResponse(userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("Profil introuvable")));
    }

    @Transactional
    public UserResponse updateProfile(UUID keycloakId, UpdateProfileRequest request) {
        var user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new NotFoundException("Profil introuvable"));
        var kcIdStr = keycloakId.toString();
        var kcUser = keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).toRepresentation();
        kcUser.setFirstName(request.firstName());
        kcUser.setLastName(request.lastName());
        keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).update(kcUser);

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        userRepository.save(user);

        return toResponse(user);
    }

    @Transactional
    public UserResponse registerClient(CreateUserRequest request, String ip) {
        return createUser(new CreateUserRequest(
                request.username(), request.email(),
                request.firstName(), request.lastName(),
                request.password(), "CLIENT_PORTAL", request.phone()), ip);
    }

    private void assignRealmRole(String userId, String role) {
        var realm = keycloak.realm(keycloakProperties.realm());
        var roleRep = realm.roles().get(role).toRepresentation();
        realm.users().get(userId).roles().realmLevel().add(List.of(roleRep));
    }

    private static CredentialRepresentation passwordCredential(String password) {
        var cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);
        return cred;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getKeycloakId(),
                user.getUsername(), user.getEmail(),
                user.getFirstName(), user.getLastName(),
                user.getRole(), user.getPhone(),
                user.getActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
