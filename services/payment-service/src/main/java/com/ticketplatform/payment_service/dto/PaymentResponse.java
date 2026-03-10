package com.ticketplatform.payment_service.dto;

import com.ticketplatform.payment_service.domain.Payment;
import com.ticketplatform.payment_service.domain.PaymentStatus;

import java.time.LocalDateTime;

public record PaymentResponse(Long id,
                              Long reservationId,
                              Long userId,
                              Long amount,
                              PaymentStatus status,
                              LocalDateTime paidAt) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getReservationId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getPaidAt()
        );
    }
}
