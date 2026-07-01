package tg.ngstars.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    @Size(min = 6) String password,
    @NotBlank String role,
    String department,
    String phone
) {}
