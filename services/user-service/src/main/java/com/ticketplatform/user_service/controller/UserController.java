package com.ticketplatform.user_service.controller;

import com.ticketplatform.user_service.dto.CreateUserRequest;
import com.ticketplatform.user_service.dto.LoginRequest;
import com.ticketplatform.user_service.dto.UserResponse;
import com.ticketplatform.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/users")
    public UserResponse createUser(@RequestBody CreateUserRequest request) {
        return userService.createUser(request);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginRequest request) {
        String message = userService.login(request);
        return Map.of("message", message);
    }

    @GetMapping("/users/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}

