package com.ticketplatform.payment_service.event;

import java.math.BigDecimal;

public record ReservationCreatedEvent(Long reservationId,
                                      Long userId,
                                      Long eventId,
                                      String seatNumber,
                                      Long amount) {
}
