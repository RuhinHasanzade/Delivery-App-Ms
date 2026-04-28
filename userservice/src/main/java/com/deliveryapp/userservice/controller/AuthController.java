package com.deliveryapp.userservice.controller;

import com.deliveryapp.userservice.dto.request.LoginRequest;
import com.deliveryapp.userservice.dto.request.RefreshRequest;
import com.deliveryapp.userservice.dto.request.RegisterRequest;
import com.deliveryapp.userservice.dto.response.AuthResponse;
import com.deliveryapp.userservice.entity.UserEntity;
import com.deliveryapp.userservice.service.UserAuthService;
import com.deliveryapp.userservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserAuthService userAuthService;

    public AuthController(UserService userService, UserAuthService userAuthService) {

        this.userService = userService;
        this.userAuthService = userAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userAuthService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {

        AuthResponse response = userAuthService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<String> createAdmin(@RequestBody RegisterRequest request) {
        userService.createAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
    }

    @PostMapping("/courier/create")
    public ResponseEntity<String> createCourier(@RequestBody RegisterRequest request) {
        userService.createCourier(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Courier created successfully");
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest request) {
        return ResponseEntity.ok(userAuthService.refreshToken(request.refreshToken()));
    }

}
