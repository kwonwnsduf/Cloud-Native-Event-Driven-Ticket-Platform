package com.ticketplatform.reservation_service.producer;

import com.ticketplatform.reservation_service.event.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationEventProducer {
    private static final String TOPIC = "reservation-created";

    private final KafkaTemplate<String, Object> kafkaTemplate;
    public void sendReservationCreated(ReservationCreatedEvent event) {

        try {
            var result = kafkaTemplate
                    .send(TOPIC, String.valueOf(event.reservationId()), event)
                    .get();

            log.info("reservation-created sent. topic={}, partition={}, offset={}, event={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    event);
        } catch (Exception e) {
            log.error("failed to send reservation-created. event={}", event, e);
            throw new RuntimeException(e);
        }
    }
}
