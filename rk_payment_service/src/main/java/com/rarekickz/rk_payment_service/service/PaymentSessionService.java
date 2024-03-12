package com.rarekickz.rk_payment_service.service;

import com.rarekickz.rk_payment_service.dto.WebhookDTO;

public interface PaymentSessionService {

    void create(String orderId, String sessionId);

    void processWebhook(WebhookDTO webhookDTO);
}
