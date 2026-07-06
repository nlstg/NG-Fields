package tg.ngstars.interv.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
    @NotBlank String type,
    @NotBlank String description,
    @Min(1) Integer quantity,
    BigDecimal unitPrice
) {}
