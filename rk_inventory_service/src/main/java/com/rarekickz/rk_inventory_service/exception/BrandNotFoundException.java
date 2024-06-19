package com.rarekickz.rk_inventory_service.exception;

public class BrandNotFoundException extends RuntimeException {

    public BrandNotFoundException(Long brandId) {
        super(String.format("Brand with ID: [%s] not found", brandId));
    }
}
