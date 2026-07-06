package tg.ngstars.interv.dto;

public record UpdateEquipmentRequest(
    String brand,
    String model,
    String serial,
    String location,
    String problemDescription,
    String openprojectTicketId,
    String openprojectTicketUrl
) {}
