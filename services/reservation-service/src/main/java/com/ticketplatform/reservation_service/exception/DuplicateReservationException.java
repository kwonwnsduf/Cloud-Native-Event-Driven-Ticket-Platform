package com.ticketplatform.reservation_service.exception;

public class DuplicateReservationException extends RuntimeException{
    public DuplicateReservationException(String message) {
        super(message);
    }
}
