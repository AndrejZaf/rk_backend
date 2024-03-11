package com.rarekickz.rk_payment_service.service;

import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface StripeService {
    Session generateSession(String orderId, OrderDetailsDTO orderDetails) throws StripeException;

    void registerWebhooks();

    void clearWebhooks();
}
