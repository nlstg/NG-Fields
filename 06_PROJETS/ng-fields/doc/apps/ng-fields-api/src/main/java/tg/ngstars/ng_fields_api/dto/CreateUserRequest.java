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
