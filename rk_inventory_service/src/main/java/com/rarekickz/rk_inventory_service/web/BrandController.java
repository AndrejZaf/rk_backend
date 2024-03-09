package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.dto.BrandDTO;
import com.rarekickz.rk_inventory_service.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.rarekickz.rk_inventory_service.converter.BrandConverter.convertToBrandDTOList;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/brand")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        final List<Brand> brands = brandService.findAll();
        return new ResponseEntity<>(convertToBrandDTOList(brands), HttpStatus.OK);
    }
}
