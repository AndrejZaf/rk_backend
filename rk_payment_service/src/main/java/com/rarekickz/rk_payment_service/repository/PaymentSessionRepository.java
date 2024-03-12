package com.rarekickz.rk_payment_service.repository;

import com.rarekickz.rk_payment_service.domain.PaymentSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentSessionRepository extends JpaRepository<PaymentSession, Long> {

    boolean existsByOrderIdAndStripeSessionId(String orderId, String stripeSessionId);

    Optional<PaymentSession> findByStripeSessionId(String stripeSessionId);

    void deleteByStripeSessionId(String stripeSessionId);
}
