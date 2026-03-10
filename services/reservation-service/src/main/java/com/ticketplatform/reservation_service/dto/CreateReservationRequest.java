package com.ticketplatform.reservation_service.dto;

public record CreateReservationRequest( Long userId,
                                        Long eventId,
                                        String seatNumber) {
}
