package com.ticketplatform.payment_service.event;

public record ReservationCreatedEvent( Long reservationId,
                                       Long userId,
                                       Long eventId,
                                       String seatNumber) {
}
