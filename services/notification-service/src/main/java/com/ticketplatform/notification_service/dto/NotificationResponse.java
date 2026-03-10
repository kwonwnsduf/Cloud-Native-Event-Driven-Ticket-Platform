package com.ticketplatform.notification_service.dto;

import com.ticketplatform.notification_service.domain.Notification;
import com.ticketplatform.notification_service.domain.NotificationStatus;
import com.ticketplatform.notification_service.domain.NotificationType;

import java.time.LocalDateTime;

public record NotificationResponse(Long id,
                                   Long userId,
                                   Long reservationId,
                                   NotificationType type,
                                   String message,
                                   NotificationStatus status,
                                   LocalDateTime sentAt) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getReservationId(),
                notification.getType(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getSentAt()
        );
    }
}
