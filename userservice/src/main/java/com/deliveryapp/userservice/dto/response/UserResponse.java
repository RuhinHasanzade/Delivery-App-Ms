package com.deliveryapp.userservice.dto.response;

import com.deliveryapp.userservice.entity.Role;

import java.util.UUID;


public record UserResponse(UUID uuid,
                           String fullname,
                           String username,
                           String email,
                           Role role
                               ) {
}
