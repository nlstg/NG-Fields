package tg.ngstars.ng_fields_api.dto;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public record CreateInterventionRequest(

    String localId,
    @NotNull Instant date,
    @NotNull UUID clientId,
    String type,

    LocalTime departureTime,
    LocalTime arrivalTime,
    LocalTime interventionStartTime,
    LocalTime interventionEndTime,
    LocalTime returnTime,

    String problemDesc,
    String openProjectTicketId,
    String diagnosis,

    String workDone,
    String equipmentType,
    String equipmentBrand,
    String equipmentModel,
    String equipmentSerial,
    String equipmentLocation,

    String result,

    String recommendations,

    Boolean billable,
    String billingNotes
) {}
