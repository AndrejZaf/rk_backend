package com.rarekickz.rk_payment_service.dto;

import lombok.Data;

@Data
public class WebhookDTO {

    private String type;
    private PaymentDataDTO data;
}
