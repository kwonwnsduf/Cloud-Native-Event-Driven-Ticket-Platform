package com.ticketplatform.reservation_service.dto;

import com.ticketplatform.reservation_service.domain.Reservation;
import com.ticketplatform.reservation_service.domain.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(Long id,
                                  Long userId,
                                  Long eventId,
                                  String seatNumber,
                                  ReservationStatus status,
                                  LocalDateTime reservedAt,
                                  LocalDateTime cancelledAt) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getUserId(),
                reservation.getEventId(),
                reservation.getSeatNumber(),
                reservation.getStatus(),
                reservation.getReservedAt(),
                reservation.getCancelledAt()
        );
    }
}
