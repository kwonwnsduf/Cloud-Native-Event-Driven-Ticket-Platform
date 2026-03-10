package com.ticketplatform.reservation_service.service;

import com.ticketplatform.reservation_service.domain.Reservation;
import com.ticketplatform.reservation_service.domain.ReservationStatus;
import com.ticketplatform.reservation_service.dto.CreateReservationRequest;
import com.ticketplatform.reservation_service.dto.ReservationResponse;
import com.ticketplatform.reservation_service.exception.DuplicateReservationException;
import com.ticketplatform.reservation_service.exception.ReservationNotFoundException;
import com.ticketplatform.reservation_service.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationService {
    private final ReservationRepository reservationRepository;

    @Transactional
    public ReservationResponse createReservation(CreateReservationRequest request) {
        validateRequest(request);

        boolean alreadyReserved = reservationRepository.existsByEventIdAndSeatNumberAndStatus(
                request.eventId(),
                request.seatNumber(),
                ReservationStatus.RESERVED
        );

        if (alreadyReserved) {
            throw new DuplicateReservationException("이미 예약된 좌석입니다.");
        }

        Reservation reservation = Reservation.builder()
                .userId(request.userId())
                .eventId(request.eventId())
                .seatNumber(request.seatNumber())
                .status(ReservationStatus.RESERVED)
                .build();

        try {
            Reservation saved = reservationRepository.save(reservation);
            return ReservationResponse.from(saved);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateReservationException("동시에 같은 좌석 예약이 시도되어 예약에 실패했습니다.");
        }
    }

    public ReservationResponse getReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ReservationNotFoundException("예약을 찾을 수 없습니다. id=" + id));

        return ReservationResponse.from(reservation);
    }

    @Transactional
    public ReservationResponse cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findByIdAndStatus(id, ReservationStatus.RESERVED)
                .orElseThrow(() -> new ReservationNotFoundException("취소 가능한 예약을 찾을 수 없습니다. id=" + id));

        reservation.cancel();

        return ReservationResponse.from(reservation);
    }

    private void validateRequest(CreateReservationRequest request) {
        if (request.userId() == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (request.eventId() == null) {
            throw new IllegalArgumentException("eventId는 필수입니다.");
        }
        if (request.seatNumber() == null || request.seatNumber().isBlank()) {
            throw new IllegalArgumentException("seatNumber는 필수입니다.");
        }
    }
}
