package com.rarekickz.rk_payment_service.service;

import com.stripe.exception.StripeException;

public interface PaymentService {

    String generateSessionUrl(String id) throws StripeException;
}
