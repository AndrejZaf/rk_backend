package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.dto.CustomerDetailsDTO;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.dto.ProductDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.WebhookEndpoint;
import com.stripe.model.WebhookEndpointCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.WebhookEndpointCreateParams;
import com.stripe.param.WebhookEndpointListParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
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
        try (MockedStatic<Customer> customerStatic = mockStatic(Customer.class);
             MockedStatic<Session> sessionStatic = mockStatic(Session.class);) {
            Customer customer = new Customer();
            customer.setId("customerId");
            customerStatic.when(() -> Customer.create((CustomerCreateParams) any())).thenReturn(customer);
            Session session = new Session();
            session.setId("sessionId");
            sessionStatic.when(() -> Session.create((SessionCreateParams) any())).thenReturn(session);

            // Act
            Session actualSession = stripeService.generateSession(orderId);

            // Assert
            verify(paymentSessionService).create(orderId, session.getId());
            assertThat(actualSession.getId(), is(equalTo(session.getId())));
        }
    }

    @Test
    void registerWebhooks_successfullyRegistersWebhooks() throws StripeException {
        // Arrange
        try (MockedStatic<WebhookEndpoint> webhookStatic = mockStatic(WebhookEndpoint.class);) {
            WebhookEndpoint webhookEndpoint = mock(WebhookEndpoint.class);
            WebhookEndpointCollection webhookEndpointCollection = new WebhookEndpointCollection();
            webhookEndpointCollection.setData(List.of(webhookEndpoint));
            webhookStatic.when(() -> WebhookEndpoint.list((WebhookEndpointListParams) any())).thenReturn(webhookEndpointCollection);

            // Act
            stripeService.registerWebhooks();

            // Assert
            webhookStatic.verify(() -> WebhookEndpoint.create((WebhookEndpointCreateParams) any()));
        }
    }

    @Test
    void clearWebhooks_throwsException() throws StripeException {
        // Arrange
        try (MockedStatic<WebhookEndpoint> webhookStatic = mockStatic(WebhookEndpoint.class);) {
            WebhookEndpoint webhookEndpoint = mock(WebhookEndpoint.class);
            WebhookEndpointCollection webhookEndpointCollection = new WebhookEndpointCollection();
            webhookEndpointCollection.setData(List.of(webhookEndpoint));
            webhookStatic.when(() -> WebhookEndpoint.list((WebhookEndpointListParams) any())).thenReturn(webhookEndpointCollection);
            doThrow(new InvalidRequestException("test", null, null, null, 0, null)).when(webhookEndpoint).delete();

            // Act
            // Assert
            assertThrows(RuntimeException.class, () -> stripeService.clearWebhooks());
        }
    }
}
