package az.woltclone.orderms.dto.notification;

import java.util.UUID;

public record CreateNotificationRequest(
    UUID userId,
    String title,
    String message
){}
