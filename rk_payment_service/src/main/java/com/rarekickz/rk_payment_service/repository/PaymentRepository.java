package com.rarekickz.rk_payment_service.repository;

import com.rarekickz.rk_payment_service.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
