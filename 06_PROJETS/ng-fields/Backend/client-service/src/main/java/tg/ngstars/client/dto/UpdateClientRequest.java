package tg.ngstars.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateClientRequest(
    @NotBlank @Size(max = 200) String companyName,
    @Size(max = 150) String contactName,
    @NotBlank @Email @Size(max = 150) String email,
    @Size(max = 30) String phone,
    String address,
    Double latitude,
    Double longitude
) {}
