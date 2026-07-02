# 04 — API de création de compte (Keycloak Admin API)

**Objectif :** Créer des utilisateurs dans Keycloak via l'API REST Spring Boot
**Temps estimé :** 30 minutes
**Dépend de :** [03-spring-security.md](03-spring-security.md)
**Priorité :** 🔴 CRITIQUE — À terminer avant le 2 juin au soir

---

## Flux de création de compte

```
POST /api/admin/users (ADMIN)
  → Spring Boot UserService
    → keycloak-admin-client → Keycloak Admin API
      → Création user + assignation rôle
    → Enregistrement dans PostgreSQL (optionnel, table users)
  → Retourne 201 Created + détails
```

```
POST /api/public/register (ANONYME)
  → Spring Boot UserService
    → keycloak-admin-client → Keycloak Admin API
      → Création user + rôle CLIENT_PORTAL
  → Retourne 201 Created + instructions
```

---

## Étape 1 : Ajouter @EnableConfigurationProperties dans la classe principale

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/NgFieldsApiApplication.java`

```java
@SpringBootApplication
@EnableConfigurationProperties(KeycloakProperties.class)
public class NgFieldsApiApplication {
    ...
}
```

## Étape 2 : Créer KeycloakProperties

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/config/KeycloakProperties.java`

```java
package tg.ngstars.ng_fields_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(
    String authServerUrl,
    String realm,
    String adminClientId,
    String adminClientSecret
) {}
```

> Ce record résout les warnings IntelliJ sur `keycloak.*` et fournit de l'autocomplete.

## Étape 3 : Créer KeycloakAdminConfig

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/config/KeycloakAdminConfig.java`

```java
package tg.ngstars.ng_fields_api.config;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Bean
    public Keycloak keycloakAdmin(KeycloakProperties props) {
        return KeycloakBuilder.builder()
            .serverUrl(props.authServerUrl())
            .realm(props.realm())
            .clientId(props.adminClientId())
            .clientSecret(props.adminClientSecret())
            .grantType("client_credentials")
            .build();
    }
}
```

> Spring injecte automatiquement `KeycloakProperties` dans la méthode du bean. Plus de `@Value`.

## Étape 4 : Créer les DTOs

### CreateUserRequest

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/dto/CreateUserRequest.java`

```java
package tg.ngstars.ng_fields_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank @Size(min = 3, max = 50)
    String username,

    @NotBlank @Email
    String email,

    @NotBlank
    String firstName,

    @NotBlank
    String lastName,

    @NotBlank @Size(min = 10)
    String password,

    String role
) {}
```

### UserResponse

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/dto/UserResponse.java`

```java
package tg.ngstars.ng_fields_api.dto;

public record UserResponse(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    String role,
    boolean enabled
) {}
```

> `role` n'est pas `@NotBlank` car l'inscription publique ne passe pas de rôle (forcé à `CLIENT_PORTAL` côté serveur).

## Étape 5 : Créer UserService

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/service/UserService.java`

```java
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

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final Keycloak keycloakAdmin;
    private final String realm;

    public UserService(Keycloak keycloakAdmin, KeycloakProperties props) {
        this.keycloakAdmin = keycloakAdmin;
        this.realm = props.realm();
    }

    public UserResponse createUser(CreateUserRequest request) {
        RealmResource realmResource = keycloakAdmin.realm(realm);

        List<UserRepresentation> existing = realmResource.users()
            .searchByUsername(request.username(), true);
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

        return new UserResponse(userId, request.username(), request.email(),
            request.firstName(), request.lastName(), request.role(), true);
    }
}
```

## Étape 6 : Créer UserController

**Fichier :** `src/main/java/tg/ngstars/ng_fields_api/controller/UserController.java`

```java
package tg.ngstars.ng_fields_api.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tg.ngstars.ng_fields_api.dto.CreateUserRequest;
import tg.ngstars.ng_fields_api.dto.UserResponse;
import tg.ngstars.ng_fields_api.service.UserService;

import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("ADMIN creating user: username={}, role={}", request.username(), request.role());
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/api/public/register")
    public ResponseEntity<?> registerClient(@Valid @RequestBody CreateUserRequest request) {
        log.info("Public registration: username={}", request.username());
        CreateUserRequest portalRequest = new CreateUserRequest(
            request.username(),
            request.email(),
            request.firstName(),
            request.lastName(),
            request.password(),
            "CLIENT_PORTAL"
        );

        UserResponse response = userService.createUser(portalRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "message", "Compte créé avec succès. Vous pouvez vous connecter sur le portail client.",
            "user", response
        ));
    }
}
```

## Étape 7 : Créer les dossiers

```powershell
New-Item -ItemType Directory -Path "src\main\java\tg\ngstars\ng_fields_api\dto" -Force
New-Item -ItemType Directory -Path "src\main\java\tg\ngstars\ng_fields_api\service" -Force
```

## Étape 8 : Configurer le Client Secret

Dans `application.yaml`, le secret du client `ng-fields-backend` :

```yaml
keycloak:
  admin-client-secret: COPIE_ICI_LE_SECRET
```

Pour trouver le secret :
1. Aller dans Admin Console → Clients → `ng-fields-backend` → Credentials
2. Cliquer **Regenerate Secret** si nécessaire
3. Copier la valeur

## Étape 9 : Lancer et tester

### Démarrer Spring Boot

```powershell
cd F:\03_Pro_IT\07_Clients\NG-STARs\06_PROJETS\Projet_NG-Fields\apps\ng-fields-api
.\mvnw.cmd spring-boot:run
```

### Test 1 : Créer un ADMIN

```powershell
# Obtenir un token pour le client ng-fields-backend
$tokenResponse = curl -X POST http://localhost:8080/realms/ng-fields/protocol/openid-connect/token `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "client_id=ng-fields-backend" `
  -d "client_secret=COPIE_ICI_LE_SECRET" `
  -d "grant_type=client_credentials" | ConvertFrom-Json
$adminToken = $tokenResponse.access_token

# Créer un ADMIN via l'API
curl -X POST http://localhost:8081/api/admin/users `
  -H "Authorization: Bearer $adminToken" `
  -H "Content-Type: application/json" `
  -d '{
    "username": "superadmin",
    "email": "super@ng-fields.test",
    "firstName": "Super",
    "lastName": "Admin",
    "password": "StrongPass123!",
    "role": "ADMIN"
  }'
```

**Résultat attendu :** `201 Created`
```json
{
  "id": "a1b2c3d4-...",
  "username": "superadmin",
  "email": "super@ng-fields.test",
  "firstName": "Super",
  "lastName": "Admin",
  "role": "ADMIN",
  "enabled": true
}
```

### Test 2 : Inscription publique

```powershell
curl -X POST http://localhost:8081/api/public/register `
  -H "Content-Type: application/json" `
  -d '{
    "username": "client_dupont",
    "email": "dupont@example.com",
    "firstName": "Jean",
    "lastName": "Dupont",
    "password": "MonMotDePasse123!"
  }'
```

**Résultat attendu :** `201 Created`
```json
{
  "message": "Compte créé avec succès. Vous pouvez vous connecter sur le portail client.",
  "user": {
    "id": "...",
    "username": "client_dupont",
    "role": "CLIENT_PORTAL",
    ...
  }
}
```

### Test 3 : Vérifier dans Keycloak

1. Aller dans Admin Console → Realm `ng-fields` → Users
2. Vérifier que `superadmin` et `client_dupont` apparaissent
3. Vérifier que les rôles sont corrects

### Test 4 : Login OIDC avec le nouveau compte

```powershell
curl -X POST http://localhost:8080/realms/ng-fields/protocol/openid-connect/token `
  -H "Content-Type: application/x-www-form-urlencoded" `
  -d "client_id=ng-fields-web" `
  -d "username=client_dupont" `
  -d "password=MonMotDePasse123!" `
  -d "grant_type=password"
```

**Résultat attendu :** Un `access_token` valide
```json
{
  "access_token": "eyJ...",
  "expires_in": 900,
  "refresh_token": "eyJ...",
  "token_type": "Bearer"
}
```

### Test 5 : API protégée avec le nouveau token

```powershell
# Décoder le token pour vérifier le rôle
$token = "..."  # le access_token du test 4
$parts = $token.Split('.')
$decoded = [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($parts[1]))
Write-Output $decoded
# Doit contenir : "realm_access": {"roles": ["CLIENT_PORTAL"]}

# Appeler l'API avec le token
curl -H "Authorization: Bearer $token" http://localhost:8081/api/public/health
# Doit fonctionner (endpoint public)
```

---

## ✅ Critères de succès pour le 2 juin au soir

| Critère | Test |
|---------|------|
| Un ADMIN peut créer un compte | `POST /api/admin/users` → 201 |
| Un client peut s'inscrire | `POST /api/public/register` → 201 |
| Le compte existe dans Keycloak | Admin Console → Users |
| Le rôle est correct | Vérifier dans le JWT → `realm_access.roles` |
| Login OIDC fonctionne | `curl /token` → access_token valide |
| API protégée avec token fonctionne | `curl -H "Authorization: Bearer \$TOKEN" ...` |

---

## Prochaine étape (après création de compte validée)

➡️ [05-client-crud.md](05-client-crud.md) — CRUD Clients
