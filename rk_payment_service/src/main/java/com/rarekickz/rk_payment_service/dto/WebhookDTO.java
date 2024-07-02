package com.rarekickz.rk_payment_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WebhookDTO {

    @NotNull
    private String type;

    @NotNull
    private PaymentDataDTO data;
}
