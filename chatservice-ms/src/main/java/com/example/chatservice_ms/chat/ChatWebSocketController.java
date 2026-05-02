package com.example.chatservice_ms.chat;

import com.example.chatservice_ms.auth.AuthenticatedUser;
import com.example.chatservice_ms.auth.UserRole;
import com.example.chatservice_ms.chat.dto.ChatMessageResponse;
import com.example.chatservice_ms.chat.dto.SendMessageRequest;
import com.example.chatservice_ms.websocket.StompPrincipal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChatService chatService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload SendMessageRequest request,
                            Principal principal) {

        StompPrincipal stompPrincipal = (StompPrincipal) principal;

        AuthenticatedUser sender = new AuthenticatedUser(
                stompPrincipal.name(),
                UserRole.valueOf(stompPrincipal.role())
        );

        ChatMessageResponse response =
                chatService.sendMessage(
                        sender,
                        request,
                        stompPrincipal.authorization()
                );

        messagingTemplate.convertAndSend(
                "/topic/orders/" + request.orderId(),
                response
        );
    }
}
