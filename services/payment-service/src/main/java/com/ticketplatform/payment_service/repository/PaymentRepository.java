package com.ticketplatform.payment_service.repository;

import com.ticketplatform.payment_service.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment,Long> {

}
