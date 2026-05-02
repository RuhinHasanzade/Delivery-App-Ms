package az.woltclone.orderms.dto.user;

import java.util.UUID;

public record UserResponse(
        UUID uuid,
        String fullname,
        String username,
        String email,
        String role
) {}
