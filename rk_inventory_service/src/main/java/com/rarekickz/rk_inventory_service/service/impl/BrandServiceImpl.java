package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.exception.BrandNotFoundException;
import com.rarekickz.rk_inventory_service.repository.BrandRepository;
import com.rarekickz.rk_inventory_service.service.BrandService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public List<Brand> findAll() {
        log.debug("Retrieving all brands from the database");
        return brandRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Brand findById(final Long brandId) {
        log.debug("Retrieving brand by ID: [{}]", brandId);
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException(brandId));
    }
}
