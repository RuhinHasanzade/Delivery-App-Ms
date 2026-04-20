package com.deliveryapp.payment_ms.dto;


import com.deliveryapp.payment_ms.entity.PaymentMethod;
import com.deliveryapp.payment_ms.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PaymentResponse {
    private UUID id;
    private UUID orderId;
    private UUID userId;
    private BigDecimal amount;
    private PaymentStatus status;
    private PaymentMethod method;
    private String transactionId;
    private String providerResponse;
    private LocalDateTime createdAt;
}
