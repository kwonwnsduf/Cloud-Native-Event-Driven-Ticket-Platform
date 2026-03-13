package com.ticketplatform.payment_service.service;

import com.ticketplatform.payment_service.domain.Payment;
import com.ticketplatform.payment_service.domain.PaymentStatus;
import com.ticketplatform.payment_service.dto.CreatePaymentRequest;
import com.ticketplatform.payment_service.dto.PaymentResponse;
import com.ticketplatform.payment_service.event.PaymentCompletedEvent;
import com.ticketplatform.payment_service.event.ReservationCreatedEvent;
import com.ticketplatform.payment_service.exception.PaymentNotFoundException;
import com.ticketplatform.payment_service.producer.PaymentEventProducer;
import com.ticketplatform.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        validateRequest(request);

        PaymentStatus status = request.amount()>0
                ? PaymentStatus.COMPLETED
                : PaymentStatus.FAILED;

        Payment payment = Payment.builder()
                .reservationId(request.reservationId())
                .userId(request.userId())
                .amount(request.amount())
                .status(status)
                .build();

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.from(saved);
    }
    @Transactional
    public void completePaymentFromReservationEvent(ReservationCreatedEvent event) {
        log.error("=== PAYMENT SERVICE START === reservationId={}, amount={}",
                event.reservationId(), event.amount());

        if (event.reservationId() == null || event.userId() == null || 
            event.seatNumber() == null || event.seatNumber().isBlank() || event.amount() == null) {
            log.error("=== INVALID EVENT DATA === event={}", event);
            return;
        }

        if (paymentRepository.existsByReservationId(event.reservationId())) {
            log.error("=== PAYMENT ALREADY EXISTS === reservationId={}", event.reservationId());
            return;
        }



        PaymentStatus status = event.amount()> 0
                ? PaymentStatus.COMPLETED
                : PaymentStatus.FAILED;

        Payment payment = Payment.builder()
                .reservationId(event.reservationId())
                .userId(event.userId())
                .amount(event.amount())
                .status(status)
                .build();

        Payment saved = paymentRepository.save(payment);

        log.error("=== PAYMENT SAVED === paymentId={}, status={}, amount={}",
                saved.getId(), saved.getStatus(), saved.getAmount());

        if (saved.getStatus() == PaymentStatus.COMPLETED) {
            paymentEventProducer.sendPaymentCompleted(
                    new PaymentCompletedEvent(
                            saved.getId(),
                            saved.getReservationId(),
                            saved.getUserId(),
                            saved.getAmount(),
                            saved.getStatus().name()
                    )
            );
            log.error("=== PAYMENT COMPLETED EVENT SENT === paymentId={}", saved.getId());
        }
    }

    public PaymentResponse getPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("결제를 찾을 수 없습니다. id=" + id));

        return PaymentResponse.from(payment);
    }

    private void validateRequest(CreatePaymentRequest request) {
        if (request.reservationId() == null) {
            throw new IllegalArgumentException("reservationId는 필수입니다.");
        }
        if (request.userId() == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (request.amount() == null) {
            throw new IllegalArgumentException("amount는 필수입니다.");
        }
    }
    private void validateReservationEvent(ReservationCreatedEvent event) {
        if (event.reservationId() == null) {
            throw new IllegalArgumentException("reservationId는 필수입니다.");
        }
        if (event.userId() == null) {
            throw new IllegalArgumentException("userId는 필수입니다.");
        }
        if (event.seatNumber() == null || event.seatNumber().isBlank()) {
            throw new IllegalArgumentException("seatNumber는 필수입니다.");
        }
        if (event.amount() == null) {
            throw new IllegalArgumentException("amount는 필수입니다.");
        }

    }
}
