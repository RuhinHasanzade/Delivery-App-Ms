package com.deliveryapp.payment_ms.controller;



import com.deliveryapp.payment_ms.dto.CreatePaymentRequest;
import com.deliveryapp.payment_ms.dto.PaymentResponse;
import com.deliveryapp.payment_ms.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse create(@RequestBody @Valid CreatePaymentRequest request) {
        return paymentService.create(request);
    }

    @GetMapping("/{id}")
    public PaymentResponse getById(@PathVariable UUID id) {
        return paymentService.getById(id);
    }

    @GetMapping("/order/{orderId}")
    public PaymentResponse getByOrderId(@PathVariable UUID orderId) {
        return paymentService.getByOrderId(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<PaymentResponse> getByUserId(@PathVariable UUID userId) {
        return paymentService.getByUserId(userId);
    }

    @PatchMapping("/{id}/success")
    public PaymentResponse markSuccess(@PathVariable UUID id,
                                       @RequestParam String transactionId,
                                       @RequestParam(required = false) String providerResponse) {
        return paymentService.markSuccess(id, transactionId, providerResponse);
    }

    @PatchMapping("/{id}/failed")
    public PaymentResponse markFailed(@PathVariable UUID id,
                                      @RequestParam(required = false) String providerResponse) {
        return paymentService.markFailed(id, providerResponse);
    }
}