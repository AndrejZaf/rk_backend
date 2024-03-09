package com.rarekickz.rk_payment_service.service.impl;

import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.dto.ProductDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import com.rarekickz.rk_payment_service.service.PaymentService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api-key}")
    private String STRIPE_API_KEY;

    @Value("${client.base-url}")
    private String clientBaseUrl;

    private final ExternalOrderService externalOrderService;

    @Override
    public String generateSessionUrl(String id) throws StripeException {
        Stripe.apiKey = STRIPE_API_KEY;
        String clientBaseURL = clientBaseUrl;
        OrderDetailsDTO orderDetails = externalOrderService.getOrderDetails(id);

        Customer customer = Customer.create(CustomerCreateParams.builder()
                .setName(orderDetails.getCustomerDetails().getName())
                .setEmail(orderDetails.getCustomerDetails().getEmail())
                .build());
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setCustomer(customer.getId())
                        .setSuccessUrl(clientBaseURL + "/success?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(clientBaseURL + "/failure");

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
                                            .setUnitAmountDecimal(BigDecimal.valueOf(product.getPrice()))
                                            .build())
                            .build());
        }

        Session session = Session.create(paramsBuilder.build());

        return session.getUrl();
    }
}
