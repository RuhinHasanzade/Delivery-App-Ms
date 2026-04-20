package com.deliveryapp.payment_ms.service;

import com.deliveryapp.payment_ms.client.OrderClient;
import com.deliveryapp.payment_ms.dto.CreatePaymentRequest;
import com.deliveryapp.payment_ms.dto.PaymentResponse;
import com.deliveryapp.payment_ms.entity.Payment;
import com.deliveryapp.payment_ms.entity.PaymentStatus;
import com.deliveryapp.payment_ms.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;

    public PaymentResponse create(CreatePaymentRequest request) {
        System.out.println("CREATE START");

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .userId(request.getUserId())
                .amount(request.getAmount())
                .method(request.getMethod())
                .status(PaymentStatus.PENDING)
                .build();

        System.out.println("BEFORE SAVE");

        Payment saved = paymentRepository.saveAndFlush(payment);

        System.out.println("AFTER SAVE ID = " + saved.getId());

        return toResponse(saved);
    }

    public PaymentResponse getById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));
        return toResponse(payment);
    }

    public PaymentResponse getByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order: " + orderId));
        return toResponse(payment);
    }

    public List<PaymentResponse> getByUserId(UUID userId) {
        return paymentRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PaymentResponse markSuccess(UUID id, String transactionId, String providerResponse) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(transactionId);
        payment.setProviderResponse(providerResponse);

        Payment saved = paymentRepository.save(payment);

        orderClient.updateStatus(payment.getOrderId(), "CONFIRMED");

        return toResponse(saved);
    }

    public PaymentResponse markFailed(UUID id, String providerResponse) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found: " + id));

        payment.setStatus(PaymentStatus.FAILED);
        payment.setProviderResponse(providerResponse);

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .method(payment.getMethod())
                .transactionId(payment.getTransactionId())
                .providerResponse(payment.getProviderResponse())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
