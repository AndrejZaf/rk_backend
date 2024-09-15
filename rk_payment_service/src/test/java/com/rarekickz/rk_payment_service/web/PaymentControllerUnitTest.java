package com.rarekickz.rk_payment_service.web;

import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentControllerUnitTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentSessionService paymentSessionService;

    @Test
    void finalizeOrder_marksTheOrderAsPaid() {
        // Arrange
        WebhookDTO webhookDTO = mock(WebhookDTO.class);

        // Act
        ResponseEntity<Void> actualPaymentResponse = paymentController.finalizeOrder(webhookDTO);

        // Assert
        assertThat(actualPaymentResponse.getStatusCode(), is(equalTo(HttpStatus.OK)));
        verify(paymentSessionService).processWebhook(webhookDTO);
    }
}
