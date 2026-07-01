package tg.ngstars.interv.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CreateInterventionRequest(
    @NotBlank String reference,
    @NotBlank UUID clientId,
    String clientName,
    String clientEmail,
    String clientPhone,
    String clientAddress,
    String equipmentType,
    String equipmentBrand,
    String equipmentModel,
    String equipmentSerial,
    String reportedIssue,
    String diagnosis,
    String status,
    OffsetDateTime interventionDate,
    UUID assignedTo,
    String siteAddress,
    String siteCity,
    BigDecimal estimatedCost,
    String notes,
    List<CreateItemRequest> items
) {

    public record CreateItemRequest(
        @NotBlank String type,
        @NotBlank String description,
        Integer quantity,
        BigDecimal unitPrice
    ) {}
}
