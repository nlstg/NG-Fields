package tg.ngstars.ng_fields_api.dto;

import tg.ngstars.ng_fields_api.model.Intervention;
import tg.ngstars.ng_fields_api.model.InterventionResult;
import tg.ngstars.ng_fields_api.model.InterventionStatus;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record InterventionResponse(

    UUID   id,
    String localId,
    Instant date,
    InterventionStatus status,
    String type,

    String technicianId,
    String technicianName,

    UUID   clientId,
    String clientName,

    LocalTime departureTime,
    LocalTime arrivalTime,
    LocalTime interventionStartTime,
    LocalTime interventionEndTime,
    LocalTime returnTime,
    Integer   durationMinutes,

    String problemDesc,
    String openProjectTicketId,
    String diagnosis,

    String workDone,
    String equipmentType,
    String equipmentBrand,
    String equipmentModel,
    String equipmentSerial,
    String equipmentLocation,

    List<InterventionItemResponse> items,

    InterventionResult result,
    String recommendations,

    Boolean billable,
    String  billingNotes,

    String signatureClientUrl,
    String signatureTechnicianUrl,
    String signatureManagerUrl,

    String  pdfUrl,
    Instant syncedAt,
    Instant createdAt,
    Instant updatedAt

) {
    public static InterventionResponse from(Intervention i) {
        return new InterventionResponse(
            i.getId(), i.getLocalId(), i.getDate(), i.getStatus(), i.getType(),
            i.getTechnicianId(), i.getTechnicianName(),
            i.getClient() != null ? i.getClient().getId() : null,
            i.getClient() != null ? i.getClient().getCompanyName() : null,
            i.getDepartureTime(), i.getArrivalTime(),
            i.getInterventionStartTime(), i.getInterventionEndTime(),
            i.getReturnTime(), i.getDurationMinutes(),
            i.getProblemDesc(), i.getOpenProjectTicketId(), i.getDiagnosis(),
            i.getWorkDone(), i.getEquipmentType(), i.getEquipmentBrand(),
            i.getEquipmentModel(), i.getEquipmentSerial(), i.getEquipmentLocation(),
            i.getItems().stream().map(InterventionItemResponse::from).toList(),
            i.getResult(), i.getRecommendations(),
            i.getBillable(), i.getBillingNotes(),
            i.getSignatureClientUrl(), i.getSignatureTechnicianUrl(), i.getSignatureManagerUrl(),
            i.getPdfUrl(), i.getSyncedAt(), i.getCreatedAt(), i.getUpdatedAt()
        );
    }
}
