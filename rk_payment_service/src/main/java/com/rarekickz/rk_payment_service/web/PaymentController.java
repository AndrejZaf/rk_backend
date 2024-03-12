package com.rarekickz.rk_payment_service.web;

import com.rarekickz.rk_payment_service.dto.StripeSessionDTO;
import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import com.rarekickz.rk_payment_service.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;
    private final PaymentSessionService paymentSessionService;

    @GetMapping("/{id}")
    ResponseEntity<StripeSessionDTO> payOrder(@PathVariable String id) throws StripeException {
        Session session = stripeService.generateSession(id);
        return new ResponseEntity<>(new StripeSessionDTO(session.getUrl()), HttpStatus.OK);
    }

    @PostMapping("/success")
    ResponseEntity<String> finalizeOrder(@RequestBody WebhookDTO webhookDTO) throws StripeException {
        paymentSessionService.processWebhook(webhookDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
