package com.rarekickz.rk_payment_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Payment session not found")
public class PaymentSessionNotFoundException extends RuntimeException {
}
