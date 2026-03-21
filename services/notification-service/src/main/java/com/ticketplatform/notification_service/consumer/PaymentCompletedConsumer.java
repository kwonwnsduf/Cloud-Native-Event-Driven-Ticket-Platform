package com.ticketplatform.notification_service.consumer;

import com.ticketplatform.notification_service.domain.Notification;
import com.ticketplatform.notification_service.domain.NotificationStatus;
import com.ticketplatform.notification_service.domain.NotificationType;
import com.ticketplatform.notification_service.event.PaymentCompletedEvent;
import com.ticketplatform.notification_service.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Profile("!test")

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedConsumer {
    private final NotificationRepository notificationRepository;

    @KafkaListener(topics = "payment-completed", groupId = "notification-group")
    public void consume(PaymentCompletedEvent event) {
        log.info("payment-completed consumed: {}", event);

        Notification notification = Notification.builder()
                .userId(event.userId())
                .reservationId(event.reservationId())
                .type(NotificationType.EMAIL)
                .message("예약 결제가 완료되었습니다. paymentId=" + event.paymentId())
                .status(NotificationStatus.SENT)
                .build();

        notificationRepository.save(notification);
    }
}
