package com.ticketplatform.payment_service.service;

import com.ticketplatform.payment_service.domain.Payment;
import com.ticketplatform.payment_service.domain.PaymentStatus;
import com.ticketplatform.payment_service.dto.CreatePaymentRequest;
import com.ticketplatform.payment_service.dto.PaymentResponse;
import com.ticketplatform.payment_service.exception.PaymentNotFoundException;
import com.ticketplatform.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {
        validateRequest(request);

        PaymentStatus status = request.amount() > 0
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
}
