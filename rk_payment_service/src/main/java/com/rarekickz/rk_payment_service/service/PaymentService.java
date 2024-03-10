package com.rarekickz.rk_payment_service.service;

import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {

    String generateSessionUrl(String id, HttpServletRequest request) throws StripeException;
}
