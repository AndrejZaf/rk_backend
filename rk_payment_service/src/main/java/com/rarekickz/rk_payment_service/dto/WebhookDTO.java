package com.rarekickz.rk_payment_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WebhookDTO {

    @NotNull
    private String type;

    @NotNull
    private PaymentDataDTO data;
}
