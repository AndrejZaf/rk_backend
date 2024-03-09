package com.rarekickz.rk_inventory_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "sneaker_image")
@NoArgsConstructor
@AllArgsConstructor
public class SneakerImage {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(name = "image_data")
    private String imageData;


    @ManyToOne
    @JoinColumn(name = "sneaker_id", nullable = false)
    private Sneaker sneaker;

    public SneakerImage(final String imageData, final Sneaker sneaker) {
        this.imageData = imageData;
        this.sneaker = sneaker;
    }
}
