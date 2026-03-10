package com.ticketplatform.notification_service.dto;

import com.ticketplatform.notification_service.domain.NotificationType;

public record CreateNotificationRequest(Long userId,
                                        Long reservationId,
                                        NotificationType type,
                                        String message) {
}
