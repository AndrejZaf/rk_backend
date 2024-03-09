package com.rarekickz.rk_inventory_service.service;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.dto.SneakerSizeDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface SneakerSizeService {

    List<Double> findAllDistinctSizes();

    Set<SneakerSize> create(Sneaker sneaker, Collection<SneakerSizeDTO> sneakerSizes);

    void delete(Collection<SneakerSize> sneakerSizes);
}
