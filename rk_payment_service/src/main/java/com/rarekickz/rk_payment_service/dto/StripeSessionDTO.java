package com.rarekickz.rk_payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StripeSessionDTO {
    private String stripeSessionUrl;
}
