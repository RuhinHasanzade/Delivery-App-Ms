package com.deliveryapp.payment_ms.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "order-service", url = "http://localhost:8085")
public interface OrderClient {

    @PatchMapping("/api/orders/{id}/internal/status")
    void updateStatus(@PathVariable UUID id,
                      @RequestParam("status") String status);
}