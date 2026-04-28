package az.woltclone.orderms.client;

import az.woltclone.orderms.dto.notification.CreateNotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationservice-ms" , url = "${services.notification.url}")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    void createNotification(@RequestBody CreateNotificationRequest notification);
}
