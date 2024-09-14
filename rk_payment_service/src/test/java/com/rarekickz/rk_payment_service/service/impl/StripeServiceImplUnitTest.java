package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.dto.CustomerDetailsDTO;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.dto.ProductDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StripeServiceImplUnitTest {

    @InjectMocks
    private StripeServiceImpl stripeService;

    @Mock
    private ExternalOrderService externalOrderService;

    @Mock
    private PaymentSessionService paymentSessionService;

    @Test
    void generateSession_successfullyCreatesPaymentSession() throws StripeException {
        // Arrange
        String orderId = "id";
        OrderDetailsDTO orderDetails = OrderDetailsDTO.builder()
                .customerDetails(CustomerDetailsDTO.builder()
                        .email("test@example.com")
                        .name("Test")
                        .build())
                .products(List.of(ProductDTO.builder()
                        .id(1L)
                        .name("Test")
                        .price(100.0)
                        .build()))
                .build();
        when(externalOrderService.getOrderDetails(orderId)).thenReturn(orderDetails);
        ReflectionTestUtils.setField(StripeServiceImpl.class, "stripeApiKey", "test");

        // Act
        Session actualSession = stripeService.generateSession(orderId);

        // Assert
//        verify(paymentSessionService).create(orderId, session.getId());
//        assertThat(actualSession.getId(), is(equalTo(session.getId())));
    }
}
