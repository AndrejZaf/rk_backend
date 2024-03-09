package com.rarekickz.rk_payment_service.web;

import com.rarekickz.rk_payment_service.service.PaymentService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/{id}")
    ResponseEntity<String> payOrder(@PathVariable String id) throws StripeException {
        paymentService.generateSessionUrl(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
