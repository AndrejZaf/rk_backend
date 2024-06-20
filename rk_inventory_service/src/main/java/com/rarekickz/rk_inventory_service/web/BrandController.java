package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.dto.BrandDTO;
import com.rarekickz.rk_inventory_service.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.rarekickz.rk_inventory_service.converter.BrandConverter.convertToBrandDTOList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/brands")
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<BrandDTO>> getAllBrands() {
        log.info("Received a request to get all brands");
        final List<Brand> brands = brandService.findAll();
        return new ResponseEntity<>(convertToBrandDTOList(brands), HttpStatus.OK);
    }
}
