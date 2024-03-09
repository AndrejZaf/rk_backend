package com.rarekickz.rk_inventory_service.service;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SneakerImageService {

    List<SneakerImage> findAllBySneakerIds(Collection<Long> sneakerIds);

    Set<SneakerImage> create(Collection<String> imageData, Sneaker sneaker);

    void delete(Collection<SneakerImage> sneakerImages);
}
