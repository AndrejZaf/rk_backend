package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.service.SneakerSizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/sneaker-sizes")
public class SneakerSizeController {

    private final SneakerSizeService sneakerSizeService;

    @GetMapping
    public ResponseEntity<List<Double>> getSneakerSizes() {
        log.info("Received a request to get all sneaker sizes");
        final List<Double> sneakerSizes = sneakerSizeService.findAllDistinctSizes();
        return new ResponseEntity<>(sneakerSizes, HttpStatus.OK);
    }
}
