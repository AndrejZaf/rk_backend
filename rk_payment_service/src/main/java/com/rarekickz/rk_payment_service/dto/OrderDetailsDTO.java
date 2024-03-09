package com.rarekickz.rk_payment_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrderDetailsDTO {

    CustomerDetailsDTO customerDetails;
    List<ProductDTO> products;
}
