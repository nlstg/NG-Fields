package tg.ngstars.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.RoleScopeResource;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tg.ngstars.auth.config.KeycloakProperties;
import tg.ngstars.auth.dto.CreateUserRequest;
import tg.ngstars.auth.dto.UpdateProfileRequest;
import tg.ngstars.auth.exception.ConflictException;
import tg.ngstars.auth.exception.NotFoundException;
import tg.ngstars.auth.model.User;
import tg.ngstars.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;
    @Mock AuditService auditService;
    @Mock Keycloak keycloak;
    @Mock RealmResource realm;
    @Mock UsersResource usersResource;
    @Mock UserResource userResource;
    @Mock RolesResource rolesResource;
    @Mock RoleResource roleResource;
    @Mock RoleMappingResource roleMappingResource;
    @Mock RoleScopeResource roleScopeResource;

    UserService service;
    KeycloakProperties props = new KeycloakProperties("http://localhost:8088", "admin-cli", "secret", "ng-fields");

    UUID keycloakId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository, auditService, keycloak, props);
        lenient().when(keycloak.realm(props.realm())).thenReturn(realm);
        lenient().when(realm.users()).thenReturn(usersResource);
        lenient().when(usersResource.get(keycloakId.toString())).thenReturn(userResource);
        lenient().when(userResource.roles()).thenReturn(roleMappingResource);
        lenient().when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        lenient().when(realm.roles()).thenReturn(rolesResource);
    }

    private User user() {
        var u = new User();
        u.setId(userId);
        u.setKeycloakId(keycloakId);
        u.setUsername("jdoe");
        u.setEmail("j@doe.com");
        u.setFirstName("John");
        u.setLastName("Doe");
        u.setRole("TECHNICIAN");
        u.setActive(true);
        return u;
    }

    @Test
    void createUser_shouldCreateInKeycloakAndDb() throws Exception {
        var req = new CreateUserRequest("jdoe", "j@doe.com", "John", "Doe", "pass123", "TECHNICIAN", null);

        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(userRepository.existsByEmail("j@doe.com")).thenReturn(false);

        var locationUri = new URI("http://localhost:8088/admin/realms/ng-fields/users/" + keycloakId);
        var response = mock(Response.class);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(locationUri);
        when(usersResource.create(any())).thenReturn(response);

        when(rolesResource.get("TECHNICIAN")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());

        when(userRepository.save(any())).thenAnswer(i -> {
            var u = (User) i.getArgument(0);
            u.setId(userId);
            return u;
        });

        var result = service.createUser(req, "admin");

        assertEquals("jdoe", result.username());
        assertEquals("TECHNICIAN", result.role());
        verify(auditService).log(any(), eq("USER_CREATED"), eq("User"), anyString(), anyString(), eq("admin"));
    }

    @Test
    void createUser_duplicateUsername_throwsConflict() {
        var req = new CreateUserRequest("jdoe", "j@doe.com", "John", "Doe", "pass123", "TECHNICIAN", null);
        when(userRepository.existsByUsername("jdoe")).thenReturn(true);

        assertThrows(ConflictException.class, () -> service.createUser(req, "admin"));
    }

    @Test
    void getUser_shouldReturnUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user()));
        var result = service.getUser(userId);
        assertEquals("jdoe", result.username());
    }

    @Test
    void getUser_notFound_throwsNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> service.getUser(userId));
    }

    @Test
    void getAllUsers_shouldReturnList() {
        when(userRepository.findAll()).thenReturn(List.of(user()));
        var result = service.getAllUsers();
        assertEquals(1, result.size());
    }

    @Test
    void assignRole_shouldUpdateRole() {
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user()));
        when(roleScopeResource.listAll()).thenReturn(List.of());
        when(rolesResource.get("MANAGER")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.assignRole(keycloakId, "MANAGER", "admin");
        assertEquals("MANAGER", result.role());
    }

    @Test
    void updateUserStatus_enable_shouldActivate() {
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user()));
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.updateUserStatus(keycloakId, true, "admin");

        assertTrue(result.active());
        verify(auditService).log(any(), eq("ACCOUNT_ENABLED"), eq("User"), anyString(), anyString(), eq("admin"));
    }

    @Test
    void getProfile_shouldReturnByKeycloakId() {
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user()));
        var result = service.getProfile(keycloakId);
        assertEquals("jdoe", result.username());
    }

    @Test
    void updateProfile_shouldUpdateNames() {
        var request = new UpdateProfileRequest("Jane", "Smith");
        when(userRepository.findByKeycloakId(keycloakId)).thenReturn(Optional.of(user()));
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        var result = service.updateProfile(keycloakId, request);

        assertEquals("Jane", result.firstName());
        assertEquals("Smith", result.lastName());
    }

    @Test
    void deleteUser_shouldDisable() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user()));
        when(userResource.toRepresentation()).thenReturn(new UserRepresentation());
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.deleteUser(userId, "admin");

        verify(auditService).log(any(), eq("USER_DELETED"), eq("User"), anyString(), contains("desactive"), eq("admin"));
    }

    @Test
    void registerClient_createsWithClientPortalRole() throws Exception {
        var req = new CreateUserRequest("client1", "c@test.com", "Client", "One", "pass123", "ADMIN", null);

        when(userRepository.existsByUsername("client1")).thenReturn(false);
        when(userRepository.existsByEmail("c@test.com")).thenReturn(false);

        var locationUri = new URI("http://localhost:8088/admin/realms/ng-fields/users/" + keycloakId);
        var response = mock(Response.class);
        when(response.getStatus()).thenReturn(201);
        when(response.getLocation()).thenReturn(locationUri);
        when(usersResource.create(any())).thenReturn(response);

        when(rolesResource.get("CLIENT_PORTAL")).thenReturn(roleResource);
        when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());

        when(userRepository.save(any())).thenAnswer(i -> {
            var u = (User) i.getArgument(0);
            u.setId(userId);
            return u;
        });

        var result = service.registerClient(req, "127.0.0.1");

        assertEquals("CLIENT_PORTAL", result.role());
    }
}
