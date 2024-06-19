package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.dto.SneakerSizeDTO;
import com.rarekickz.rk_inventory_service.repository.SneakerSizeRepository;
import com.rarekickz.rk_inventory_service.service.SneakerSizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SneakerSizeServiceImpl implements SneakerSizeService {

    private final SneakerSizeRepository sneakerSizeRepository;

    @Override
    public List<Double> findAllDistinctSizes() {
        log.debug("Retrieving all distinct sneaker sizes from the database");
        return sneakerSizeRepository.findAllDistinctSizes();
    }

    @Override
    public Set<SneakerSize> create(final Sneaker sneaker, final Collection<SneakerSizeDTO> sneakerSizes) {
        final List<SneakerSize> sneakerSizeCollection = sneakerSizes.stream()
                .map(sneakerSize -> new SneakerSize(sneaker, sneakerSize.getSize(), sneakerSize.getQuantity()))
                .toList();
        return new HashSet<>(sneakerSizeRepository.saveAll(sneakerSizeCollection));
    }

    @Override
    public void delete(final Collection<SneakerSize> sneakerSizes) {
        sneakerSizeRepository.deleteAll(sneakerSizes);
        sneakerSizeRepository.flush();
    }
}
