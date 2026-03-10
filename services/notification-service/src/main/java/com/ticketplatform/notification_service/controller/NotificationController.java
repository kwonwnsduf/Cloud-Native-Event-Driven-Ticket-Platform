package com.ticketplatform.notification_service.controller;

import com.ticketplatform.notification_service.dto.CreateNotificationRequest;
import com.ticketplatform.notification_service.dto.NotificationResponse;
import com.ticketplatform.notification_service.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse createNotification(@RequestBody CreateNotificationRequest request) {
        return notificationService.createNotification(request);
    }

    @GetMapping("/{id}")
    public NotificationResponse getNotification(@PathVariable Long id) {
        return notificationService.getNotification(id);
    }
}

