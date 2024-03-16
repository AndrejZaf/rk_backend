package com.rarekickz.rk_payment_service.web;

import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentSessionService paymentSessionService;

    @PostMapping
    ResponseEntity<String> finalizeOrder(@RequestBody WebhookDTO webhookDTO) {
        paymentSessionService.processWebhook(webhookDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
