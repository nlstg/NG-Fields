package tg.ngstars.auth.service;

import java.util.List;
import java.util.UUID;

import jakarta.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import tg.ngstars.auth.config.KeycloakProperties;
import tg.ngstars.auth.dto.CreateUserRequest;
import tg.ngstars.auth.dto.UserResponse;
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
    private final SecurityUtils securityUtils;

    public UserService(UserRepository userRepository, AuditService auditService,
            Keycloak keycloak, KeycloakProperties keycloakProperties,
            SecurityUtils securityUtils) {
        this.userRepository = userRepository;
        this.auditService = auditService;
        this.keycloak = keycloak;
        this.keycloakProperties = keycloakProperties;
        this.securityUtils = securityUtils;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request, String ip) {
        if (userRepository.existsByEmail(request.email()))
            throw new IllegalArgumentException("Email already exists");

        var kcUser = new UserRepresentation();
        kcUser.setUsername(request.email());
        kcUser.setEmail(request.email());
        kcUser.setFirstName(request.name());
        kcUser.setEnabled(true);
        if (request.password() != null)
            kcUser.setCredentials(List.of(createPasswordCredential(request.password())));

        var realm = keycloak.realm(keycloakProperties.realm());
        try (Response response = realm.users().create(kcUser)) {
            if (response.getStatus() != 201)
                throw new RuntimeException("Keycloak create failed: " + response.getStatus());

            var location = response.getLocation();
            var keycloakId = UUID.fromString(location.getPath().substring(location.getPath().lastIndexOf('/') + 1));

            if (request.role() != null)
                assignRealmRole(keycloakId.toString(), request.role());

            var user = userRepository.save(User.builder()
                    .keycloakId(keycloakId)
                    .name(request.name())
                    .email(request.email())
                    .role(request.role())
                    .department(request.department())
                    .phone(request.phone())
                    .active(true)
                    .build());

            auditService.log(securityUtils.getCurrentUserId(), "CREATE_USER", "User",
                    user.getId().toString(), "Created user " + user.getEmail(), ip);

            return toResponse(user);
        }
    }

    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getUser(UUID id) {
        return toResponse(userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id)));
    }

    @Transactional
    public UserResponse updateUser(UUID id, CreateUserRequest request, String ip) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        var kcIdStr = user.getKeycloakId().toString();
        var kcUser = keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).toRepresentation();
        kcUser.setUsername(request.email());
        kcUser.setEmail(request.email());
        kcUser.setFirstName(request.name());
        keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).update(kcUser);

        if (request.password() != null)
            keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr)
                    .resetPassword(createPasswordCredential(request.password()));
        if (request.role() != null && !request.role().equals(user.getRole()))
            assignRealmRole(kcIdStr, request.role());

        user.setName(request.email());
        user.setEmail(request.email());
        user.setRole(request.role());
        user.setDepartment(request.department());
        user.setPhone(request.phone());
        user = userRepository.save(user);

        auditService.log(securityUtils.getCurrentUserId(), "UPDATE_USER", "User",
                user.getId().toString(), "Updated user " + user.getEmail(), ip);

        return toResponse(user);
    }

    @Transactional
    public void deleteUser(UUID id, String ip) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + id));
        var kcIdStr = user.getKeycloakId().toString();
        var kcUser = keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).toRepresentation();
        kcUser.setEnabled(false);
        keycloak.realm(keycloakProperties.realm()).users().get(kcIdStr).update(kcUser);
        user.setActive(false);
        userRepository.save(user);
        auditService.log(securityUtils.getCurrentUserId(), "DELETE_USER", "User",
                user.getId().toString(), "Deactivated user " + user.getEmail(), ip);
    }

    public UserResponse getProfile() {
        var userId = securityUtils.getCurrentUserId();
        return toResponse(userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found")));
    }

    @Transactional
    public UserResponse updateProfile(String name, String phone, String ip) {
        var userId = securityUtils.getCurrentUserId();
        var user = userRepository.findByKeycloakId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        user.setName(name);
        user.setPhone(phone);
        user = userRepository.save(user);
        auditService.log(userId, "UPDATE_PROFILE", "User",
                user.getId().toString(), "Updated own profile", ip);
        return toResponse(user);
    }

    @Transactional
    public UserResponse registerClient(CreateUserRequest request, String ip) {
        return createUser(new CreateUserRequest(
                request.name(), request.email(), request.password(),
                "CLIENT_PORTAL", null, null), ip);
    }

    private void assignRealmRole(String userId, String role) {
        var realm = keycloak.realm(keycloakProperties.realm());
        var roleRep = realm.roles().get(role).toRepresentation();
        realm.users().get(userId).roles().realmLevel().add(List.of(roleRep));
    }

    private static CredentialRepresentation createPasswordCredential(String password) {
        var cred = new CredentialRepresentation();
        cred.setType(CredentialRepresentation.PASSWORD);
        cred.setValue(password);
        cred.setTemporary(false);
        return cred;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getKeycloakId(), user.getName(),
                user.getEmail(), user.getRole(), user.getDepartment(), user.getPhone(),
                user.getActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
