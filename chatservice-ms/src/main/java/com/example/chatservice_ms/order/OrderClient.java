package com.example.chatservice_ms.order;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "order-service",
        url = "${chat.order-service.base-url}"
)
public interface OrderClient {

    @GetMapping("/internal/orders/{orderId}/chat-context")
    OrderChatContext getOrderChatContext(
            @PathVariable String orderId,
            @RequestHeader("Authorization") String authorization
    );
}
