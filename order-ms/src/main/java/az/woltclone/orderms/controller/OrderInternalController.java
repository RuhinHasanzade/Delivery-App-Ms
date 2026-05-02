package az.woltclone.orderms.controller;

import az.woltclone.orderms.dto.chat.OrderChatContext;
import az.woltclone.orderms.dto.order.OrderResponse;
import az.woltclone.orderms.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/internal/orders")
@RequiredArgsConstructor
public class OrderInternalController {

    private final OrderService orderService;

    @GetMapping("/{orderId}/chat-context")
    public OrderChatContext getOrderChatContext(@PathVariable UUID orderId) {

        OrderResponse order = orderService.getOrderById(orderId);

        return new OrderChatContext(
                order.getId().toString(),
                order.getUserId().toString(),
                order.getCourierId().toString()
        );
    }
}