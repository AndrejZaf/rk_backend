package com.rarekickz.rk_payment_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDetailsDTO {

    private String name;
    private String email;
}
