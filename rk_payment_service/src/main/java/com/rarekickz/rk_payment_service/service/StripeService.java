package com.rarekickz.rk_payment_service.service;

import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

public interface StripeService {

    Session generateSession(String orderId) throws StripeException;

    void registerWebhooks() throws StripeException;

    void clearWebhooks() throws StripeException;
}
