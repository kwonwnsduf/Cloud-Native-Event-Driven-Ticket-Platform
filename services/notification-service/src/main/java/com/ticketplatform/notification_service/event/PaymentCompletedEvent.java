package com.ticketplatform.notification_service.event;

public record PaymentCompletedEvent(Long paymentId,
                                    Long reservationId,
                                    Long userId,
                                    Long amount,
                                    String status) {
}
