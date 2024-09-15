package com.rarekickz.rk_inventory_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class SneakerDTO {

    private Long id;

    @NotNull
    private Long brandId;

    @NotNull
    private Integer gender;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String description;

    @NotNull
    private Double price;

    @NotEmpty
    private List<String> images;
    private List<SneakerSizeDTO> sizes;
    private String brand;
    private Boolean special;
}
