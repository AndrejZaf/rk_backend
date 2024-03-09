package com.rarekickz.rk_inventory_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SneakerSizeId implements Serializable {

    @Column(name = "sneaker_id")
    private Long sneakerId;

    @Column(name = "size")
    private Double size;
}
