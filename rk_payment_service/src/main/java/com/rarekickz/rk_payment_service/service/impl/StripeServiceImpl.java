package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import com.rarekickz.rk_payment_service.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.WebhookEndpoint;
import com.stripe.model.WebhookEndpointCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.WebhookEndpointCreateParams;
import com.stripe.param.WebhookEndpointListParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api-key}")
    private static String stripeApiKey;

    @Value("${client.base-url}")
    private String clientBaseUrl;

    private final ExternalOrderService externalOrderService;
    private final PaymentSessionService paymentSessionService;

    static {
        Stripe.apiKey = stripeApiKey;
    }

    @Override
    public Session generateSession(final String orderId) throws StripeException {
        log.debug("Creating a stripe session for order ID: [{}]", orderId);
        final OrderDetailsDTO orderDetails = externalOrderService.getOrderDetails(orderId);
        final Customer customer = Customer.create(CustomerCreateParams.builder()
                .setName(orderDetails.getCustomerDetails().getName())
                .setEmail(orderDetails.getCustomerDetails().getEmail())
                .build());
        final SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCustomer(customer.getId())
                .setSuccessUrl(String.format("%s/shopping-cart/success/%s", clientBaseUrl, orderId))
                .setCancelUrl(String.format("%s/failure/%s", clientBaseUrl, orderId));
        orderDetails.getProducts().forEach(product -> paramsBuilder.addLineItem(SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .putMetadata("app_id", String.valueOf(product.getId()))
                                .setName(product.getName())
                                .build())
                        .setCurrency(SessionCreateParams.PaymentMethodOptions.AcssDebit.Currency.USD.toString())
                        .setUnitAmount(product.getPrice().longValue() * 100L)
                        .build())
                .build()));
        final Session session = Session.create(paramsBuilder.build());
        paymentSessionService.create(orderId, session.getId());
        return session;
    }

    @Override
    @PostConstruct
    public void registerWebhooks() throws StripeException {
        log.debug("Registering Stripe webhooks");
        clearWebhooks();
        WebhookEndpointCreateParams params =
                WebhookEndpointCreateParams.builder()
                        .addEnabledEvent(WebhookEndpointCreateParams.EnabledEvent.CHECKOUT__SESSION__COMPLETED)
                        .addEnabledEvent(WebhookEndpointCreateParams.EnabledEvent.CHECKOUT__SESSION__EXPIRED)
                        .setUrl("https://5044-46-217-144-36.ngrok-free.app/api/payment")
                        .build();
        WebhookEndpoint.create(params);
    }

    @Override
    @PreDestroy
    public void clearWebhooks() throws StripeException {
        log.debug("Destroying Stripe webhooks");
        final WebhookEndpointListParams params = WebhookEndpointListParams.builder().setLimit(3L).build();
        final WebhookEndpointCollection webhookEndpoints = WebhookEndpoint.list(params);
        webhookEndpoints.getData().forEach(webhookEndpoint -> {
            try {
                webhookEndpoint.delete();
            } catch (StripeException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
