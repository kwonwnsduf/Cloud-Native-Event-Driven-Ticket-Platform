package com.ticketplatform.reservation_service.repository;

import com.ticketplatform.reservation_service.domain.Reservation;
import com.ticketplatform.reservation_service.domain.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    boolean existsByEventIdAndSeatNumberAndStatus(
            Long eventId,
            String seatNumber,
            ReservationStatus status
    );

    Optional<Reservation> findByIdAndStatus(Long id, ReservationStatus status);
}
