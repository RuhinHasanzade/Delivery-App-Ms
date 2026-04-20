package az.woltclone.orderms.dto.order;

import az.woltclone.orderms.entity.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
public class OrderResponse {
    private UUID id;
    private UUID userId;
    private UUID courierId;
    private BigDecimal totalAmount;
    private String deliveryAddress;
    private Double deliveryLat;
    private Double deliveryLng;
    private Double deliveryDistanceKm;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemResponse> items;
}
