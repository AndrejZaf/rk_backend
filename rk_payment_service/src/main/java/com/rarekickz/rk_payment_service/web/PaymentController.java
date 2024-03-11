package com.rarekickz.rk_payment_service.web;

import com.rarekickz.rk_payment_service.service.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
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
        String stripeSessionUrl = paymentService.generateSessionUrl(id);
        return new ResponseEntity<>(stripeSessionUrl, HttpStatus.OK);
    }

    @GetMapping("{id}/success")
    ResponseEntity<String> finalizeOrder(@PathVariable String id, HttpServletRequest request) throws StripeException {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
