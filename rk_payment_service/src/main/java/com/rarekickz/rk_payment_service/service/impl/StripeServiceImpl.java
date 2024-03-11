package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.dto.ProductDTO;
import com.rarekickz.rk_payment_service.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripeServiceImpl implements StripeService {

    @Value("${stripe.api-key}")
    private String STRIPE_API_KEY;

    @Value("${client.base-url}")
    private String clientBaseUrl;

    @Override
    public Session generateSession(String orderId, OrderDetailsDTO orderDetails) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        Customer customer = Customer.create(CustomerCreateParams.builder()
                .setName(orderDetails.getCustomerDetails().getName())
                .setEmail(orderDetails.getCustomerDetails().getEmail())
                .build());
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setCustomer(customer.getId())
                        .setSuccessUrl(String.format("%s/success/%s/", clientBaseUrl, id))
                        .setCancelUrl(String.format("%s/failure", clientBaseUrl));
        for (ProductDTO product : orderDetails.getProducts()) {
            paramsBuilder.addLineItem(
                    SessionCreateParams.LineItem.builder()
                            .setQuantity(1L)
                            .setPriceData(
                                    SessionCreateParams.LineItem.PriceData.builder()
                                            .setProductData(
                                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                            .putMetadata("app_id", String.valueOf(product.getId()))
                                                            .setName(product.getName())
                                                            .build())
                                            .setCurrency(SessionCreateParams.PaymentMethodOptions.AcssDebit.Currency.USD.toString())
                                            .setUnitAmount(product.getPrice().longValue() * 100L)
                                            .build())
                            .build());
        }
        return Session.create(paramsBuilder.build());
    }

    @Override
    @PostConstruct
    public void registerWebhooks() {

    }

    @Override
    @PreDestroy
    public void clearWebhooks() {

    }
}
