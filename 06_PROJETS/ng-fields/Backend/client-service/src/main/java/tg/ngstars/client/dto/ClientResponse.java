package tg.ngstars.client.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ClientResponse(
    UUID id,
    String name,
    String email,
    String phone,
    String address,
    String city,
    String postalCode,
    String country,
    String siret,
    String notes,
    boolean active,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
