package tg.ngstars.ng_fields_api.dto;

import tg.ngstars.ng_fields_api.model.InterventionPhoto;
import tg.ngstars.ng_fields_api.model.PhotoType;

import java.time.Instant;
import java.util.UUID;

public record PhotoResponse(
    UUID      id,
    String    url,
    PhotoType type,
    Double    latitude,
    Double    longitude,
    Instant   takenAt,
    String    originalFilename,
    Instant   createdAt
) {
    public static PhotoResponse from(InterventionPhoto p) {
        return new PhotoResponse(
            p.getId(), p.getUrl(), p.getType(),
            p.getLatitude(), p.getLongitude(),
            p.getTakenAt(), p.getOriginalFilename(),
            p.getCreatedAt()
        );
    }
}
