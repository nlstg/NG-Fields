package tg.ngstars.ng_fields_api.dto;

import tg.ngstars.ng_fields_api.model.Client;

import java.time.Instant;
import java.util.UUID;

public record ClientResponse(
    UUID     id,
    String   companyName,
    String   email,
    String   phone,
    String   address,
    Double   latitude,
    Double   longitude,
    String   contactName,
    String   contactPhone,
    Boolean  active,
    Instant  createdAt,
    Instant  updatedAt
) {
    public static ClientResponse from(Client c) {
        return new ClientResponse(
            c.getId(), c.getCompanyName(), c.getEmail(), c.getPhone(),
            c.getAddress(), c.getLatitude(), c.getLongitude(),
            c.getContactName(), c.getContactPhone(),
            c.getActive(), c.getCreatedAt(), c.getUpdatedAt()
        );
    }
}
