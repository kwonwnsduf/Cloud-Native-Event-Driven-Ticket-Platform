package com.ticketplatform.event_service.dto;

import com.ticketplatform.event_service.domain.Seat;
import com.ticketplatform.event_service.domain.SeatStatus;

public record SeatResponse(Long id,
                           String seatNumber,
                           SeatStatus status) {
    public static SeatResponse from(Seat seat) {
        return new SeatResponse(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getStatus()
        );
    }
}
