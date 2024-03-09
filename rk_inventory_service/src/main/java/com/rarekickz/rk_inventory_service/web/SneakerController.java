package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.dto.SneakerCartDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertPremiumSneaker;
import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertToSneakerCartDTOs;
import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertToSneakerDTO;
import static com.rarekickz.rk_inventory_service.converter.SneakerConverter.convertToSneakerDTOList;

@RequestMapping("/api/inventory/sneaker")
@RestController
@RequiredArgsConstructor
public class SneakerController {

    private final SneakerService sneakerService;

    @GetMapping("/premium")
    public ResponseEntity<SneakerDTO> getPremiumSneaker() {
        final Sneaker sneaker = sneakerService.findPremiumSneaker();
        return new ResponseEntity<>(convertPremiumSneaker(sneaker), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SneakerDTO> getPremiumSneaker(@PathVariable Long id) {
        final Sneaker sneaker = sneakerService.findById(id);
        return new ResponseEntity<>(convertToSneakerDTO(sneaker), HttpStatus.OK);
    }

    @GetMapping("/cart")
    public ResponseEntity<List<SneakerCartDTO>> getSneakerForCart(@RequestParam List<Long> ids) {
        final List<Sneaker> sneakers = sneakerService.findAllByIdWithImages(ids);
        return new ResponseEntity<>(convertToSneakerCartDTOs(sneakers), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SneakerDTO>> getSneakers(@RequestParam int page, @RequestParam int size,
                                                        @RequestParam(required = false) List<Long> brandIds, @RequestParam(required = false) List<Double> sizes,
                                                        @RequestParam(required = false) List<Gender> genders) {
        final List<Sneaker> sneakers = sneakerService.findAllByPages(page, size, brandIds, genders, sizes);
        return new ResponseEntity<>(convertToSneakerDTOList(sneakers), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<SneakerDTO>> getSneakers() {
        final List<Sneaker> sneakers = sneakerService.findAll();
        return new ResponseEntity<>(convertToSneakerDTOList(sneakers), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SneakerDTO> addSneaker(@RequestBody final SneakerDTO sneakerDTO) {
        final Sneaker sneaker = sneakerService.create(sneakerDTO);
        return new ResponseEntity<>(convertToSneakerDTO(sneaker), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<SneakerDTO> editSneaker(@RequestBody final SneakerDTO sneakerDTO) {
        final Sneaker sneaker = sneakerService.update(sneakerDTO);
        return new ResponseEntity<>(convertToSneakerDTO(sneaker), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteSneaker(@RequestParam("id") final Long sneakerId) {
        sneakerService.deleteById(sneakerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<Long> premiumSneaker(@RequestParam("id") final Long sneakerId) {
        sneakerService.premium(sneakerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
