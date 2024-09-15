package com.rarekickz.rk_payment_service.web;

import com.rarekickz.rk_payment_service.dto.WebhookDTO;
import com.rarekickz.rk_payment_service.service.PaymentSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentSessionService paymentSessionService;

    @PostMapping
    @Operation(summary = "Finalize order after Stripe action")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully processed order"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Payment session not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> finalizeOrder(@Valid @RequestBody final WebhookDTO webhookDTO) {
        log.info("Received a request to process order after stripe action");
        paymentSessionService.processWebhook(webhookDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
