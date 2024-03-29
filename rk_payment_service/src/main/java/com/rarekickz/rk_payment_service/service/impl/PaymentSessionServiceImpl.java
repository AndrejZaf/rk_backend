package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.domain.PaymentSession;
import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.repository.PaymentSessionRepository;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentSessionServiceImpl implements PaymentSessionService {

    private final PaymentSessionRepository paymentSessionRepository;
    private final ExternalOrderService externalOrderService;

    @Override
    public void create(String orderId, String sessionId) {
        if (paymentSessionRepository.existsByOrderIdAndStripeSessionId(orderId, sessionId)) {
            return;
        }

        PaymentSession paymentSession = PaymentSession.builder()
                .orderId(orderId)
                .stripeSessionId(sessionId)
                .build();
        paymentSessionRepository.save(paymentSession);
    }

    @Override
    public void processWebhook(final WebhookDTO webhookDTO) {
        // TODO: Use an enum here
        switch (webhookDTO.getType()) {
            case "checkout.session.completed":
                finalizeOrder(webhookDTO.getData().getObject().getId());
                break;
            case "checkout.session.expired":
                cancelOrder(webhookDTO.getData().getObject().getId());
                break;
        }
    }

    private void finalizeOrder(final String sessionId) {
        PaymentSession paymentSession = findByStripeSessionId(sessionId);
        externalOrderService.finalizeOrder(paymentSession.getOrderId());
    }

    private void cancelOrder(final String sessionId) {
        final PaymentSession paymentSession = findByStripeSessionId(sessionId);
        paymentSessionRepository.delete(paymentSession);
        externalOrderService.cancelOrder(paymentSession.getOrderId());
    }

    private PaymentSession findByStripeSessionId(String stripeSessionId) {
        return paymentSessionRepository.findByStripeSessionId(stripeSessionId)
                .orElseThrow(EntityNotFoundException::new);
    }
}
