package com.ticketplatform.payment_service.consumer;

import com.ticketplatform.payment_service.domain.Payment;
import com.ticketplatform.payment_service.domain.PaymentStatus;
import com.ticketplatform.payment_service.event.PaymentCompletedEvent;
import com.ticketplatform.payment_service.event.ReservationCreatedEvent;
import com.ticketplatform.payment_service.producer.PaymentEventProducer;
import com.ticketplatform.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCreatedConsumer {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(topics = "reservation-created", groupId = "payment-group")
    public void consume(ReservationCreatedEvent event) {
        log.info("reservation-created consumed: {}", event);

        Payment payment = Payment.builder()
                .reservationId(event.reservationId())
                .userId(event.userId())
                .amount(100000L)
                .status(PaymentStatus.COMPLETED)
                .build();

        Payment saved = paymentRepository.save(payment);

        PaymentCompletedEvent completedEvent = new PaymentCompletedEvent(
                saved.getId(),
                saved.getReservationId(),
                saved.getUserId(),
                saved.getAmount(),
                saved.getStatus().name()
        );

        paymentEventProducer.sendPaymentCompleted(completedEvent);
    }
}
