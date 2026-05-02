package com.example.chatservice_ms.websocket;

import java.security.Principal;

public record StompPrincipal(
        String name,
        String role,
        String authorization
) implements Principal {

    @Override
    public String getName() {
        return name;
    }
}