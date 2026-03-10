package com.ticketplatform.payment_service.dto;

public record CreatePaymentRequest( Long reservationId,
                                    Long userId,
                                    Long amount) {
}
