package tg.ngstars.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateClientRequest(
    @NotBlank String name,
    @NotBlank @Email String email,
    String phone,
    String address,
    String city,
    String postalCode,
    String country,
    String siret,
    String notes
) {}
