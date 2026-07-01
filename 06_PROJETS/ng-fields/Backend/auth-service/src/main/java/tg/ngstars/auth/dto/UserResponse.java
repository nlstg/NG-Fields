package tg.ngstars.auth.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
    UUID id,
    UUID keycloakId,
    String name,
    String email,
    String role,
    String department,
    String phone,
    boolean active,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt
) {}
