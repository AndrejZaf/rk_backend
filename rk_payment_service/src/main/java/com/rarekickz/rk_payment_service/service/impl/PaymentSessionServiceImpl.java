package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.domain.PaymentSession;
import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.exception.PaymentSessionNotFoundException;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.repository.PaymentSessionRepository;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentSessionServiceImpl implements PaymentSessionService {

    private final PaymentSessionRepository paymentSessionRepository;
    private final ExternalOrderService externalOrderService;

    @Override
    public void create(final String orderId, final String sessionId) {
        log.debug("Creating a payment session for order ID: [{}]", orderId);
        boolean orderExists = paymentSessionRepository.existsPaymentSessionByOrderIdAndStripeSessionId(orderId, sessionId);
        if (orderExists) {
            return;
        }

        final PaymentSession paymentSession = PaymentSession.builder()
                .orderId(orderId)
                .stripeSessionId(sessionId)
                .build();
        paymentSessionRepository.save(paymentSession);
    }

    @Override
    public void processWebhook(final WebhookDTO webhookDTO) {
        log.debug("Processing a stripe webhook for order ID: [{}]", webhookDTO.getData().getObject().getId());
        switch (webhookDTO.getType()) {
            case "checkout.session.completed":
                finalizeOrder(webhookDTO.getData().getObject().getId());
                break;
            case "checkout.session.expired":
                cancelOrder(webhookDTO.getData().getObject().getId());
                break;
            default:
                break;
        }
    }

    private void finalizeOrder(final String sessionId) {
        log.debug("Finalizing order for session ID: [{}]", sessionId);
        final PaymentSession paymentSession = findByStripeSessionId(sessionId);
        externalOrderService.finalizeOrder(paymentSession.getOrderId());
    }

    private void cancelOrder(final String sessionId) {
        log.debug("Cancelling order for session ID: [{}]", sessionId);
        final PaymentSession paymentSession = findByStripeSessionId(sessionId);
        paymentSessionRepository.delete(paymentSession);
        externalOrderService.cancelOrder(paymentSession.getOrderId());
    }

    private PaymentSession findByStripeSessionId(final String stripeSessionId) {
        return paymentSessionRepository.findByStripeSessionId(stripeSessionId)
                .orElseThrow(PaymentSessionNotFoundException::new);
    }
}
