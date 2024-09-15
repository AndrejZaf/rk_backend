package com.rarekickz.rk_payment_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentObjectDTO {

    @NotNull
    @NotEmpty
    private String id;
}
