package tg.ngstars.interv.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ItemResponse(
    UUID id,
    String type,
    String description,
    int quantity,
    BigDecimal unitPrice,
    BigDecimal total,
    OffsetDateTime createdAt
) {}
