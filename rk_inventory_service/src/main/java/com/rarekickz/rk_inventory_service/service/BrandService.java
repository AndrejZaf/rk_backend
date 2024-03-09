package com.rarekickz.rk_inventory_service.service;

import com.rarekickz.rk_inventory_service.domain.Brand;

import java.util.List;

public interface BrandService {

    List<Brand> findAll();

    Brand findById(Long brandId);
}
