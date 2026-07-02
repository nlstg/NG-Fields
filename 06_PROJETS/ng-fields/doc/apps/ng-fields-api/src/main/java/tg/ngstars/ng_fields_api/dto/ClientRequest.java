package tg.ngstars.ng_fields_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(

    @NotBlank @Size(min = 2, max = 150)
    String companyName,

    @Email
    String email,

    @Size(max = 20)
    String phone,

    String address,
    Double latitude,
    Double longitude,
    String contactName,
    String contactPhone
) {}
