package com.ticketplatform.payment_service.consumer;

import com.ticketplatform.payment_service.domain.Payment;
import com.ticketplatform.payment_service.domain.PaymentStatus;
import com.ticketplatform.payment_service.event.PaymentCompletedEvent;
import com.ticketplatform.payment_service.event.ReservationCreatedEvent;
import com.ticketplatform.payment_service.producer.PaymentEventProducer;
import com.ticketplatform.payment_service.repository.PaymentRepository;
import com.ticketplatform.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
@Profile("!test")
@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCreatedConsumer {
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final PaymentService paymentService;

    @KafkaListener(topics = "reservation-created", groupId = "payment-group")
    public void consume(ReservationCreatedEvent event) {
        log.error("=== PAYMENT CONSUMER CALLED === event={}", event);

        paymentService.completePaymentFromReservationEvent(event);}
}
