package com.rarekickz.rk_payment_service.external.impl;

import com.rarekickz.proto.lib.OrderPaymentRequest;
import com.rarekickz.rk_payment_service.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPaymentServiceUnitTest {

    @InjectMocks
    private ExternalPaymentService externalPaymentService;

    @Mock
    private StripeService stripeService;

    @Mock
    private StreamObserver responseObserver;

    @Test
    void createPaymentSession_delegatesToStripeService() throws StripeException {
        // Arrange
        String orderId = "id";
        OrderPaymentRequest paymentRequest = OrderPaymentRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        Session session = new Session();
        session.setUrl("url");
        when(stripeService.generateSession(orderId)).thenReturn(session);

        // Act
        externalPaymentService.createPaymentSession(paymentRequest, responseObserver);

        // Assert
        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }
}
