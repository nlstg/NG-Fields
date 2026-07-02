package tg.ngstars.ng_fields_api.dto;

public record UserResponse(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    String role,
    boolean enabled
) {}
