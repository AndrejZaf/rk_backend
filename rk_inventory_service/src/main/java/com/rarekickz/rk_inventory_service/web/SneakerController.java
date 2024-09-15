package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.dto.SneakerCartDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/inventory/sneakers")
public class SneakerController {

    private final SneakerService sneakerService;

    @GetMapping("/premium")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve premium sneaker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved premium sneaker"),
            @ApiResponse(responseCode = "404", description = "Sneaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SneakerDTO> getPremiumSneaker() {
        log.info("Received request to get the premium sneaker");
        final Sneaker sneaker = sneakerService.findPremiumSneaker();
        return new ResponseEntity<>(convertPremiumSneaker(sneaker), HttpStatus.OK);
    }

    @GetMapping("/popular")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve most popular sneaker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved most popular sneaker"),
            @ApiResponse(responseCode = "404", description = "Sneaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SneakerDTO> getMostPopularSneaker() {
        log.info("Received request to get the most popular sneaker");
        final Sneaker sneaker = sneakerService.findMostPopularSneaker();
        return new ResponseEntity<>(convertPremiumSneaker(sneaker), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve sneaker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sneaker"),
            @ApiResponse(responseCode = "404", description = "Sneaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SneakerDTO> getSneaker(@PathVariable Long id) {
        log.info("Received request to get sneaker by ID: [{}]", id);
        final Sneaker sneaker = sneakerService.findById(id);
        return new ResponseEntity<>(convertToSneakerDTO(sneaker), HttpStatus.OK);
    }

    @GetMapping("/cart")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve sneaker cart data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sneaker cart data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SneakerCartDTO>> getSneakerForCart(@RequestParam List<Long> ids) {
        log.info("Received request to get sneakers for cart by IDs: [{}]", ids);
        final List<Sneaker> sneakers = sneakerService.findAllByIdWithImages(ids);
        return new ResponseEntity<>(convertToSneakerCartDTOs(sneakers), HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve page of sneakers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved page of sneakers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SneakerDTO>> getSneakers(@RequestParam final int page, @RequestParam final int size,
                                                        @RequestParam(required = false) final List<Long> brandIds,
                                                        @RequestParam(required = false) final List<Double> sizes,
                                                        @RequestParam(required = false) final List<Gender> genders,
                                                        @RequestParam(defaultValue = "name;asc") final String sort,
                                                        @RequestParam(required = false) final String name) {
        log.info("Received request to get sneakers by page: [{}], size: [{}], brandIds: [{}], genders: [{}], sizes: [{}]", page, size, brandIds, genders, sizes);
        final List<Sneaker> sneakers = sneakerService.findAllByPages(page, size, brandIds, genders, sizes, sort, name);
        return new ResponseEntity<>(convertToSneakerDTOList(sneakers), HttpStatus.OK);
    }

    @GetMapping("/all")
    @PreAuthorize("permitAll()")
    @Operation(summary = "Retrieve all sneakers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all sneakers"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SneakerDTO>> getSneakers() {
        log.info("Received request to get all sneakers");
        final List<Sneaker> sneakers = sneakerService.findAll();
        return new ResponseEntity<>(convertToSneakerDTOList(sneakers), HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Create a new sneaker")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created new sneaker"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SneakerDTO> addSneaker(@Valid @RequestBody final SneakerDTO sneakerDTO) {
        log.info("Received request to add sneaker");
        final Sneaker sneaker = sneakerService.create(sneakerDTO);
        return new ResponseEntity<>(convertToSneakerDTO(sneaker), HttpStatus.CREATED);
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Update sneaker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated sneaker"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Sneaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<SneakerDTO> editSneaker(@Valid @RequestBody final SneakerDTO sneakerDTO) {
        log.info("Received request to edit sneaker");
        final Sneaker sneaker = sneakerService.update(sneakerDTO);
        return new ResponseEntity<>(convertToSneakerDTO(sneaker), HttpStatus.OK);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete sneaker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted sneaker"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Sneaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> deleteSneaker(@RequestParam("id") final Long sneakerId) {
        log.info("Received request to delete sneaker by ID: [{}]", sneakerId);
        sneakerService.deleteById(sneakerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Set a new premium sneaker by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully set a new premium sneaker"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "404", description = "Sneaker not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Void> premiumSneaker(@RequestParam("id") final Long sneakerId) {
        log.info("Received request to set a new premium sneaker with ID: [{}]", sneakerId);
        sneakerService.premium(sneakerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
