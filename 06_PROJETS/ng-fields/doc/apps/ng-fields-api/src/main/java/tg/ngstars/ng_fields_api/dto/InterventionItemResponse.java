package tg.ngstars.ng_fields_api.dto;

import tg.ngstars.ng_fields_api.model.InterventionItem;
import java.time.Instant;
import java.util.UUID;

public record InterventionItemResponse(
    UUID id,
    String name,
    Integer quantity,
    Instant createdAt
) {
    public static InterventionItemResponse from(InterventionItem item) {
        return new InterventionItemResponse(
            item.getId(), item.getName(),
            item.getQuantity(), item.getCreatedAt()
        );
    }
}
