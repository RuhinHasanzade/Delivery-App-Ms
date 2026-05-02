package com.example.chatservice_ms.order;

import com.example.chatservice_ms.common.ApiException;
import com.example.chatservice_ms.common.UuidStrings;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceClient {

    private final OrderClient orderClient;

    public OrderServiceClient(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    public OrderChatContext getOrderChatContext(String orderId, String authorizationHeader) {
        try {
            OrderChatContext response =
                    orderClient.getOrderChatContext(orderId, authorizationHeader);

            if (response == null) {
                throw new ApiException(
                        HttpStatus.NOT_FOUND,
                        "ORDER_NOT_FOUND",
                        "Order not found."
                );
            }

            return UuidStrings.normalizeOrderChatContext(response);

        } catch (FeignException.NotFound ex) {
            throw new ApiException(
                    HttpStatus.NOT_FOUND,
                    "ORDER_NOT_FOUND",
                    "Order not found."
            );

        } catch (FeignException.Unauthorized ex) {
            throw new ApiException(
                    HttpStatus.UNAUTHORIZED,
                    "ORDER_SERVICE_UNAUTHORIZED",
                    "Order service unauthorized."
            );

        } catch (FeignException.Forbidden ex) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN,
                    "ORDER_SERVICE_FORBIDDEN",
                    "Order service forbidden."
            );

        } catch (FeignException ex) {
            throw new ApiException(
                    HttpStatus.BAD_GATEWAY,
                    "ORDER_SERVICE_ERROR",
                    "Failed to fetch order details."
            );
        }
    }
}