package az.woltclone.orderms.service;

import az.woltclone.orderms.dto.order.CreateOrderRequest;
import az.woltclone.orderms.dto.order.OrderResponse;
import az.woltclone.orderms.entity.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    OrderResponse createOrder(UUID userId , CreateOrderRequest request);
    List<OrderResponse> getMyOrders(UUID userId);
    OrderResponse getOrderById(UUID orderId);
    List<OrderResponse> getAllOrders();
    OrderResponse updateStatus(UUID orderId, OrderStatus newStatus);

}
