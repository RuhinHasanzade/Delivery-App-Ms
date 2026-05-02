package az.woltclone.orderms.service.impl;

import az.woltclone.orderms.client.MenuClient;
import az.woltclone.orderms.client.NotificationClient;
import az.woltclone.orderms.client.UserClient;
import az.woltclone.orderms.dto.common.ResultDto;
import az.woltclone.orderms.dto.menu.FoodDto;
import az.woltclone.orderms.dto.notification.CreateNotificationRequest;
import az.woltclone.orderms.dto.order.CreateOrderRequest;
import az.woltclone.orderms.dto.order.OrderItemRequest;
import az.woltclone.orderms.dto.order.OrderItemResponse;
import az.woltclone.orderms.dto.order.OrderResponse;
import az.woltclone.orderms.entity.Order;
import az.woltclone.orderms.entity.OrderItem;
import az.woltclone.orderms.entity.OrderStatus;
import az.woltclone.orderms.repository.OrderRepository;
import az.woltclone.orderms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final MenuClient menuClient;
    private final NotificationClient notificationClient;
    private final UserClient userClient;
    @Override
    public OrderResponse createOrder(UUID userId, CreateOrderRequest request) {
        List<OrderItem> items = request.getItems()
                .stream()
                .map(this::buildOrderItem)
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        ResultDto<List<az.woltclone.orderms.dto.user.UserResponse>> courierResult =
                userClient.getAllCouriers();

        if (courierResult == null
                || courierResult.getData() == null
                || courierResult.getData().isEmpty()) {
            throw new RuntimeException("Courier tapilmadi");
        }

        UUID courierId = courierResult.getData()
                .get(0)
                .uuid();

        Order order = Order.builder()
                .userId(userId)
                .courierId(courierId)
                .totalAmount(totalAmount)
                .deliveryAddress(request.getDeliveryAddress())
                .deliveryLat(request.getDeliveryLat())
                .deliveryLng(request.getDeliveryLng())
                .deliveryDistanceKm(0.0)
                .status(OrderStatus.PENDING)
                .build();

        items.forEach(item -> item.setOrder(order));
        order.setItems(items);

        Order savedOrder = orderRepository.save(order);

        notificationClient.createNotification(
                new CreateNotificationRequest(
                        userId,
                        "Sifariş yaradıldı",
                        "Sifarişiniz uğurla yaradıldı. Order ID: " + savedOrder.getId()
                )
        );

        return mapToResponse(savedOrder);
    }
    @Override
    public OrderResponse updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    @Override
    public List<OrderResponse> getCourierOrders(UUID courierId) {
        return orderRepository.findByCourierId(courierId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next) {
            return;
        }

        if (current == OrderStatus.PENDING && next == OrderStatus.CONFIRMED) {
            return;
        }

        if (current == OrderStatus.CONFIRMED && next == OrderStatus.PREPARING) {
            return;
        }

        if (current == OrderStatus.PREPARING && next == OrderStatus.READY) {
            return;
        }

        if (current == OrderStatus.READY && next == OrderStatus.ON_THE_WAY) {
            return;
        }

        if (current == OrderStatus.ON_THE_WAY && next == OrderStatus.DELIVERED) {
            return;
        }

        if ((current == OrderStatus.PENDING || current == OrderStatus.CONFIRMED)
                && next == OrderStatus.CANCELLED) {
            return;
        }

        throw new RuntimeException("Invalid status transition: " + current + " -> " + next);
    }

    @Override
    public List<OrderResponse> getMyOrders(UUID userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(UUID orderId) {
        Order response = orderRepository.findById(orderId).orElseThrow(()->  new RuntimeException("Order Not Found"));
        return mapToResponse(response);
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private OrderItem buildOrderItem(OrderItemRequest request) {
        ResultDto<FoodDto> result = menuClient.getFoodById(request.getFoodId());

        if (result == null || !result.isSuccess() || result.getData() == null) {
            throw new RuntimeException("Food tapilmadi: " + request.getFoodId());
        }

        FoodDto food = result.getData();

        if (food.getAvailable() == null || !food.getAvailable()) {
            throw new RuntimeException("Food available deyil: " + food.getName());
        }

        BigDecimal lineTotal = food.getPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        return OrderItem.builder()
                .foodId(food.getId())
                .foodName(food.getName())
                .quantity(request.getQuantity())
                .unitPrice(food.getPrice())
                .lineTotal(lineTotal)
                .build();
    }


    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .courierId(order.getCourierId())
                .totalAmount(order.getTotalAmount())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryLat(order.getDeliveryLat())
                .deliveryLng(order.getDeliveryLng())
                .status(order.getStatus())
                .items(order.getItems()
                        .stream()
                        .map(item -> OrderItemResponse.builder()
                                .foodId(item.getFoodId())
                                .foodName(item.getFoodName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .lineTotal(item.getLineTotal())
                                .build())
                        .toList())
                .build();
    }


}
