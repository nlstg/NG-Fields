package tg.ngstars.auth.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
    @NotBlank @Size(min = 3, max = 50)
    String username,

    @NotBlank @Email
    String email,

    @NotBlank @Size(max = 100)
    String firstName,

    @NotBlank @Size(max = 100)
    String lastName,

    @Size(min = 6)
    String password,

    @NotBlank
    @Pattern(regexp = "ADMIN|MANAGER|TECHNICIAN|CLIENT_PORTAL",
             message = "Role invalide : ADMIN, MANAGER, TECHNICIAN, CLIENT_PORTAL")
    String role,

    String phone
) {}
