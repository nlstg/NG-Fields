package tg.ngstars.interv.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateResultRequest(
    @NotBlank String result
) {}
