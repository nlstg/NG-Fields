package tg.ngstars.ng_fields_api.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tg.ngstars.ng_fields_api.dto.CreateUserRequest;
import tg.ngstars.ng_fields_api.dto.UserResponse;
import tg.ngstars.ng_fields_api.service.UserService;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/api/admin/me")
    public ResponseEntity<Map<String, Object>> profile(@AuthenticationPrincipal Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        return ResponseEntity.ok(Map.of(
            "sub", jwt.getSubject(),
            "username", jwt.getClaimAsString("preferred_username"),
            "email", jwt.getClaimAsString("email"),
            "firstName", jwt.getClaimAsString("given_name"),
            "lastName", jwt.getClaimAsString("family_name"),
            "roles", realmAccess != null ? realmAccess.get("roles") : java.util.List.of()
        ));
    }

    @GetMapping("/api/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listUsers() {
        log.info("ADMIN listing all users");
        return ResponseEntity.ok(userService.getUsers());
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
