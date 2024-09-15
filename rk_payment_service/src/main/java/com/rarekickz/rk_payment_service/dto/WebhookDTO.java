package com.rarekickz.rk_payment_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookDTO {

    @NotNull
    @NotBlank
    private String type;

    @NotNull
    private PaymentDataDTO data;
}
