package tg.ngstars.ng_fields_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record InterventionItemRequest(
    @NotBlank String name,
    @Min(1)  Integer quantity
) {}
