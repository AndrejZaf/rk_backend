package com.rarekickz.rk_inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SneakerSizeDTO {

    private Double size;

    private Long quantity;
}
