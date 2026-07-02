package tg.ngstars.ng_fields_api.dto;

import jakarta.validation.constraints.NotBlank;

public record SignatureRequest(
    @NotBlank String imageBase64,
    String signatoryName
) {}
