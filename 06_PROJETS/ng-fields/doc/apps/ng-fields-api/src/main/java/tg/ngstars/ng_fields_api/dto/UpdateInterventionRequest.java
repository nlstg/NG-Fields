package tg.ngstars.ng_fields_api.dto;

import java.time.LocalTime;

public record UpdateInterventionRequest(

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
