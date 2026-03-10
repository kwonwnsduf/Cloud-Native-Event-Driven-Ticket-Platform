package com.ticketplatform.notification_service.service;

import com.ticketplatform.notification_service.domain.Notification;
import com.ticketplatform.notification_service.domain.NotificationStatus;
import com.ticketplatform.notification_service.dto.CreateNotificationRequest;
import com.ticketplatform.notification_service.dto.NotificationResponse;
import com.ticketplatform.notification_service.exception.NotificationNotFoundException;
import com.ticketplatform.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        validateRequest(request);

        Notification notification = Notification.builder()
                .userId(request.userId())
                .reservationId(request.reservationId())
                .type(request.type())
                .message(request.message())
                .status(NotificationStatus.SENT)
                .build();

        Notification saved = notificationRepository.save(notification);
        return NotificationResponse.from(saved);
    }

    public NotificationResponse getNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("알림을 찾을 수 없습니다. id=" + id));

        return NotificationResponse.from(notification);
    }

    private void validateRequest(CreateNotificationRequest request) {
        if (request.userId() == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (request.reservationId() == null) {
            throw new IllegalArgumentException("reservationId는 필수입니다.");
        }
        if (request.type() == null) {
            throw new IllegalArgumentException("type은 필수입니다.");
        }
        if (request.message() == null || request.message().isBlank()) {
            throw new IllegalArgumentException("message는 필수입니다.");
        }
    }
}
