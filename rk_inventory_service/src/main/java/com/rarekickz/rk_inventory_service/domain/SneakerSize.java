package com.rarekickz.rk_inventory_service.domain;


import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "sneaker_size")
@AllArgsConstructor
@NoArgsConstructor
public class SneakerSize {

    @EmbeddedId
    private SneakerSizeId sneakerSizeId;

    @Column(name = "quantity")
    private Long quantity;

    @ManyToOne
    @MapsId("sneakerId")
    private Sneaker sneaker;

    public SneakerSize(final Sneaker sneaker, final Double size, final Long quantity) {
        this.sneakerSizeId = new SneakerSizeId(sneaker.getId(), size);
        this.sneaker = sneaker;
        this.quantity = quantity;
    }
}
