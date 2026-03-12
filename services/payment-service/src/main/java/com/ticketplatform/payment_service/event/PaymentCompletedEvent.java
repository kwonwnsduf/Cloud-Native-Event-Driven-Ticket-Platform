package com.ticketplatform.payment_service.event;

public record PaymentCompletedEvent(Long paymentId,
                                    Long reservationId,
                                    Long userId,
                                    Long amount,
                                    String status) {
}
