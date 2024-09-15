package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.domain.PaymentSession;
import com.rarekickz.rk_payment_service.dto.PaymentDataDTO;
import com.rarekickz.rk_payment_service.dto.PaymentObjectDTO;
import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.exception.PaymentSessionNotFoundException;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.repository.PaymentSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentSessionServiceImplUnitTest {

    @InjectMocks
    private PaymentSessionServiceImpl paymentSessionService;

    @Mock
    private PaymentSessionRepository paymentSessionRepository;

    @Mock
    private ExternalOrderService externalOrderService;

    private String sessionId;
    private WebhookDTO webhookDTO;
    private PaymentSession paymentSession;

    @BeforeEach
    void setup() {
        sessionId = "id";
        PaymentObjectDTO paymentObjectDTO = new PaymentObjectDTO(sessionId);
        PaymentDataDTO paymentDataDTO = new PaymentDataDTO(paymentObjectDTO);
        webhookDTO = new WebhookDTO("checkout.session.completed", paymentDataDTO);
        paymentSession = new PaymentSession(1L, UUID.randomUUID().toString(), sessionId);
    }

    @Test
    void create_orderAlreadyExists() {
        // Arrange
        String orderId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        when(paymentSessionRepository.existsPaymentSessionByOrderIdAndStripeSessionId(orderId, sessionId)).thenReturn(true);

        // Act
        paymentSessionService.create(orderId, sessionId);

        // Assert
        verify(paymentSessionRepository, never()).save(any());
    }

    @Test
    void create_successfullyCreatesSystemOrder() {
        // Arrange
        String orderId = UUID.randomUUID().toString();
        String sessionId = UUID.randomUUID().toString();
        when(paymentSessionRepository.existsPaymentSessionByOrderIdAndStripeSessionId(orderId, sessionId)).thenReturn(false);

        // Act
        paymentSessionService.create(orderId, sessionId);

        // Assert
        verify(paymentSessionRepository).save(any());
    }

    @Test
    void processWebhook_throwPaymentSessionNotFoundException() {
        // Arrange
        when(paymentSessionRepository.findByStripeSessionId(sessionId)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(PaymentSessionNotFoundException.class, () -> paymentSessionService.processWebhook(webhookDTO));
    }

    @Test
    void processWebhook_marksTheOrderAsFinalized() {
        // Arrange
        when(paymentSessionRepository.findByStripeSessionId(sessionId)).thenReturn(Optional.of(paymentSession));

        // Act
        paymentSessionService.processWebhook(webhookDTO);

        // Assert
        verify(externalOrderService).finalizeOrder(paymentSession.getOrderId());
    }

    @Test
    void processWebhook_marksTheOrderAsCanceled() {
        // Arrange
        webhookDTO.setType("checkout.session.expired");
        when(paymentSessionRepository.findByStripeSessionId(sessionId)).thenReturn(Optional.of(paymentSession));

        // Act
        paymentSessionService.processWebhook(webhookDTO);

        // Assert
        verify(paymentSessionRepository).delete(paymentSession);
        verify(externalOrderService).cancelOrder(paymentSession.getOrderId());
    }

    @Test
    void processWebhook_unrecognizedType() {
        // Arrange
        webhookDTO.setType("test");

        // Act
        paymentSessionService.processWebhook(webhookDTO);

        // Assert
        verify(paymentSessionRepository, never()).findByStripeSessionId(sessionId);
    }
}
