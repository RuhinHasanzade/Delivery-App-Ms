package az.woltclone.orderms.dto.chat;

public record OrderChatContext(
        String orderId,
        String userId,
        String courierId
) {
}