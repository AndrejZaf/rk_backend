package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import com.rarekickz.rk_inventory_service.repository.SneakerImageRepository;
import com.rarekickz.rk_inventory_service.service.SneakerImageService;
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
public class SneakerImageServiceImpl implements SneakerImageService {

    private final SneakerImageRepository sneakerImageRepository;

    @Override
    public List<SneakerImage> findAllBySneakerIds(final Collection<Long> sneakerIds) {
        log.debug("Finding sneaker images by sneaker IDs: [{}]", sneakerIds);
        return sneakerImageRepository.findBySneakerIdIn(sneakerIds);
    }

    @Override
    public Set<SneakerImage> create(final Collection<String> imageData, final Sneaker sneaker) {
        log.debug("Creating sneaker images for sneaker with ID: [{}]", sneaker.getId());
        final List<SneakerImage> sneakerImages = imageData.stream()
                .map(sneakerImage -> new SneakerImage(sneakerImage, sneaker))
                .toList();
        return new HashSet<>(sneakerImageRepository.saveAll(sneakerImages));
    }

    @Override
    public void delete(final Collection<SneakerImage> sneakerImages) {
        log.debug("Deleting sneaker images");
        sneakerImageRepository.deleteAll(sneakerImages);
        sneakerImageRepository.flush();
    }
}
