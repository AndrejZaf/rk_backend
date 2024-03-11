package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.domain.Payment;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.dto.ProductDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.repository.PaymentRepository;
import com.rarekickz.rk_payment_service.service.PaymentService;
import com.rarekickz.rk_payment_service.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final ExternalOrderService externalOrderService;
    private final PaymentRepository paymentRepository;
    private final StripeService stripeService;

    @Override
    public String generateSessionUrl(String id) throws StripeException {
        OrderDetailsDTO orderDetails = externalOrderService.getOrderDetails(id);
        Session session = stripeService.generateSession(id, orderDetails);
        Payment payment = Payment.builder()
                .orderId(id)
                .stripeSessionId(session.getId())
                .build();
        paymentRepository.save(payment);
        return session.getUrl();
    }
}
