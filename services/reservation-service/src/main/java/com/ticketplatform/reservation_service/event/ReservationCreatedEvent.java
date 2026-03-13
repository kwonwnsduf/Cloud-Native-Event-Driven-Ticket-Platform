package com.ticketplatform.reservation_service.event;

public record ReservationCreatedEvent(  Long reservationId,
                                        Long userId,
                                        Long eventId,
                                        String seatNumber,
                                        Long amount) {
}
