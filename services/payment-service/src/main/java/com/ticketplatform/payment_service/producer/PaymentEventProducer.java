package com.ticketplatform.payment_service.producer;

import com.ticketplatform.payment_service.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventProducer {
    private static final String TOPIC = "payment-completed";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPaymentCompleted(PaymentCompletedEvent event) {
        try {
            var result = kafkaTemplate
                    .send(TOPIC, String.valueOf(event.paymentId()), event)
                    .get();

            log.info("payment-completed sent. topic={}, partition={}, offset={}, event={}",
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    event);
        } catch (Exception e) {
            log.error("failed to send payment-completed. event={}", event, e);
            throw new RuntimeException(e);
        }
    }
    }

