package az.woltclone.orderms.controller;

import az.woltclone.orderms.dto.common.ResultDto;
import az.woltclone.orderms.dto.order.CreateOrderRequest;
import az.woltclone.orderms.dto.order.OrderResponse;
import az.woltclone.orderms.entity.OrderStatus;
import az.woltclone.orderms.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResultDto<OrderResponse> createOrder(@RequestBody @Valid CreateOrderRequest request,
                                                Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        OrderResponse response = orderService.createOrder(userId, request);

        return ResultDto.<OrderResponse>builder()
                .success(true)
                .message("Order created successfully")
                .data(response)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResultDto<List<OrderResponse>> getMyOrders(Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());

        List<OrderResponse> response = orderService.getMyOrders(userId);

        return ResultDto.<List<OrderResponse>>builder()
                .success(true)
                .message("My orders fetched successfully")
                .data(response)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','COURIER')")
    public ResultDto<OrderResponse> getOrderById(@PathVariable UUID id) {

        OrderResponse response = orderService.getOrderById(id);

        return ResultDto.<OrderResponse>builder()
                .success(true)
                .message("Order fetched successfully")
                .data(response)
                .errors(Collections.emptyList())
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResultDto<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> response = orderService.getAllOrders();

        return ResultDto.<List<OrderResponse>>builder()
                .success(true)
                .message("All orders fetched successfully")
                .data(response)
                .errors(Collections.emptyList())
                .build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDto<OrderResponse> updateStatus(@PathVariable UUID id,
                                                 @RequestParam OrderStatus status) {

        OrderResponse response = orderService.updateStatus(id, status);

        return ResultDto.<OrderResponse>builder()
                .success(true)
                .message("Order status updated successfully")
                .data(response)
                .errors(Collections.emptyList())
                .build();
    }

    @PatchMapping("/{id}/internal/status")
    public ResultDto<OrderResponse> updateInternalStatus(@PathVariable UUID id,
                                                         @RequestParam OrderStatus status) {

        OrderResponse response = orderService.updateStatus(id, status);

        return ResultDto.<OrderResponse>builder()
                .success(true)
                .message("Order status updated successfully")
                .data(response)
                .errors(Collections.emptyList())
                .build();
    }
}