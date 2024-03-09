package com.rarekickz.rk_inventory_service.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SneakerCartDTO {

    private Long id;
    private String name;
    private Double price;
    private List<String> images;
}
