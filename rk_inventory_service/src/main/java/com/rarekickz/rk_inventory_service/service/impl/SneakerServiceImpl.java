package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.dto.ReserveSneakerDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.exception.InvalidSizeException;
import com.rarekickz.rk_inventory_service.exception.InvalidSneakerException;
import com.rarekickz.rk_inventory_service.external.ExternalOrderService;
import com.rarekickz.rk_inventory_service.repository.SneakerRepository;
import com.rarekickz.rk_inventory_service.service.BrandService;
import com.rarekickz.rk_inventory_service.service.SneakerImageService;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import com.rarekickz.rk_inventory_service.service.SneakerSizeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.rarekickz.rk_inventory_service.specification.SneakerSpecification.createSneakerSpecification;
import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class SneakerServiceImpl implements SneakerService {

    private final SneakerRepository sneakerRepository;
    private final SneakerImageService sneakerImageService;
    private final SneakerSizeService sneakerSizeService;
    private final BrandService brandService;
    private final ExternalOrderService externalOrderService;

    @Override
    @Transactional
    public Sneaker findPremiumSneaker() {
        return sneakerRepository.findBySpecialIsTrue().orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public Sneaker findMostPopularSneaker() {
        Long sneakerId = externalOrderService.findMostPopularSneakerId();
        return sneakerRepository.findByIdWithImages(sneakerId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public Sneaker findById(final Long sneakerId) {
        return sneakerRepository.findByIdWithImages(sneakerId).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    @Transactional
    public List<Sneaker> findAllByIdWithImages(final List<Long> sneakerIds) {
        return sneakerRepository.findAllByIdWithImages(sneakerIds);
    }

    @Override
    public void reserve(final Collection<ReserveSneakerDTO> reservedSneakers) {
        final Map<Long, ReserveSneakerDTO> sneakerIdToSizes = reservedSneakers.stream()
                .collect(Collectors.toMap(ReserveSneakerDTO::getSneakerId, Function.identity()));
        final List<Long> sneakerIds = reservedSneakers.stream()
                .map(ReserveSneakerDTO::getSneakerId)
                .toList();
        final List<Sneaker> sneakers = sneakerRepository.findAllWithSizes(sneakerIds);
        sneakerIds.forEach(sneakerId -> {
            if (sneakers.stream().noneMatch(sneaker -> sneaker.getId().equals(sneakerId))) {
                throw new InvalidSneakerException(String.format("Sneaker with id %d does not exist", sneakerId));
            }
        });

        sneakers.forEach(sneaker -> {
            final Set<SneakerSize> sneakerSizes = sneaker.getSneakerSizes();
            final Double size = sneakerIdToSizes.get(sneaker.getId()).getSize();
            final SneakerSize sneakerSizeFromDb = sneakerSizes.stream()
                    .filter(sneakerSize -> sneakerSize.getSneakerSizeId().getSize().equals(size))
                    .findFirst().orElseThrow(() -> new InvalidSizeException("The selected size is not available"));
            if (sneakerSizeFromDb.getQuantity().equals(0L)) {
                throw new InvalidSizeException("The selected size is not available");
            }

            sneakerSizeFromDb.setQuantity(sneakerSizeFromDb.getQuantity() - 1);
        });
        sneakerRepository.saveAll(sneakers);
    }

    @Override
    public void cancel(Collection<ReserveSneakerDTO> reservedSneakers) {
        final List<Long> sneakerIds = reservedSneakers.stream()
                .map(ReserveSneakerDTO::getSneakerId)
                .toList();
        final Map<Long, ReserveSneakerDTO> sneakerIdToSizes = reservedSneakers.stream()
                .collect(Collectors.toMap(ReserveSneakerDTO::getSneakerId, Function.identity()));
        final List<Sneaker> sneakers = sneakerRepository.findAllWithSizes(sneakerIds);
        sneakers.forEach(sneaker -> {
            final Double size = sneakerIdToSizes.get(sneaker.getId()).getSize();
            final SneakerSize sneakerSizeFromDb = sneaker.getSneakerSizes().stream()
                    .filter(sneakerSize -> sneakerSize.getSneakerSizeId().getSize().equals(size))
                    .findFirst().orElseThrow(() -> new InvalidSizeException("The selected size is not available"));
            sneakerSizeFromDb.setQuantity(sneakerSizeFromDb.getQuantity() + 1);
        });
        sneakerRepository.saveAll(sneakers);
    }

    @Override
    public Double getSneakerPrices(final Collection<Long> sneakerIds) {
        final List<Sneaker> sneakers = sneakerRepository.findAllById(sneakerIds);
        return sneakers.stream()
                .map(Sneaker::getPrice)
                .reduce(Double::sum)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Sneaker> findAllByIds(final Collection<Long> sneakerIds) {
        return sneakerRepository.findAllById(sneakerIds);
    }

    @Override
    @Transactional
    public List<Sneaker> findAllByPages(final int page, final int size, final List<Long> brandIds, final List<Gender> genders, final List<Double> sizes) {
        final PageRequest pageRequest = PageRequest.of(page, size);
        final Specification<Sneaker> sneakerSpecification = createSneakerSpecification(brandIds, genders, sizes);
        final Page<Sneaker> sneakers = sneakerRepository.findAll(sneakerSpecification, pageRequest);
        final List<Long> sneakerIds = sneakers.stream().map(Sneaker::getId)
                .toList();
        final List<SneakerImage> sneakerImages = sneakerImageService.findAllBySneakerIds(sneakerIds);
        final Map<Long, Set<SneakerImage>> sneakerIdToSneakerImages = sneakerImages.stream()
                .collect(groupingBy(sneakerImage -> sneakerImage.getSneaker().getId(), toSet()));
        sneakers.stream()
                .forEach(sneaker -> sneaker.setSneakerImages(sneakerIdToSneakerImages.getOrDefault(sneaker.getId(), Collections.emptySet())));
        return sneakers.toList();
    }

    @Override
    @Transactional
    public Sneaker create(final SneakerDTO sneakerDTO) {
        final Brand brand = brandService.findById(sneakerDTO.getBrandId());
        final Sneaker sneaker = sneakerRepository.save(createSneaker(sneakerDTO, brand));
        final Set<SneakerImage> sneakerImages = sneakerImageService.create(sneakerDTO.getImages(), sneaker);
        final Set<SneakerSize> sneakerSizes = sneakerSizeService.create(sneaker, sneakerDTO.getSizes());
        sneaker.setSneakerImages(sneakerImages);
        sneaker.setSneakerSizes(sneakerSizes);
        return sneaker;
    }

    @Override
    @Transactional
    public Sneaker update(final SneakerDTO sneakerDTO) {
        final Brand brand = brandService.findById(sneakerDTO.getBrandId());
        Sneaker sneaker = sneakerRepository
                .findById(sneakerDTO.getId()).orElseThrow(EntityNotFoundException::new);
        sneakerImageService.delete(sneaker.getSneakerImages());
        sneakerSizeService.delete(sneaker.getSneakerSizes());
        final Set<SneakerImage> sneakerImages = sneakerImageService.create(sneakerDTO.getImages(), sneaker);
        final Set<SneakerSize> sneakerSizes = sneakerSizeService.create(sneaker, sneakerDTO.getSizes());
        updateSneakerProperties(sneakerDTO, brand, sneaker, sneakerImages, sneakerSizes);
        sneaker = sneakerRepository.save(sneaker);
        return sneaker;
    }

    @Override
    @Transactional
    public void deleteById(final Long sneakerId) {
        final Sneaker sneaker = sneakerRepository.findById(sneakerId).orElseThrow(EntityNotFoundException::new);
        sneakerSizeService.delete(sneaker.getSneakerSizes());
        sneakerImageService.delete(sneaker.getSneakerImages());
        sneakerRepository.delete(sneaker);
    }

    @Override
    public void premium(final Long sneakerId) {
        final Sneaker newSpecialSneaker = sneakerRepository.findById(sneakerId).orElseThrow(EntityNotFoundException::new);
        if (newSpecialSneaker.isSpecial()) {
            return;
        }

        newSpecialSneaker.setSpecial(true);
        final Optional<Sneaker> previousSpecialSneaker = sneakerRepository.findBySpecialTrue();
        if (previousSpecialSneaker.isPresent()) {
            final Sneaker sneaker = previousSpecialSneaker.get();
            sneaker.setSpecial(false);
            sneakerRepository.saveAll(List.of(newSpecialSneaker, sneaker));
            return;
        }

        sneakerRepository.save(newSpecialSneaker);
    }

    @Override
    @Transactional
    public List<Sneaker> findAll() {
        return sneakerRepository.findAllSneakersWithImages();
    }

    private Sneaker createSneaker(final SneakerDTO sneakerDTO, final Brand brand) {
        return Sneaker.builder()
                .brand(brand)
                .name(sneakerDTO.getName())
                .description(sneakerDTO.getDescription())
                .price(sneakerDTO.getPrice())
                .gender(Gender.values()[sneakerDTO.getGender()])
                .sneakerImages(new HashSet<>())
                .special(false)
                .build();
    }

    private void updateSneakerProperties(final SneakerDTO sneakerDTO, final Brand brand, final Sneaker sneaker, final Set<SneakerImage> sneakerImages, final Set<SneakerSize> sneakerSizes) {
        sneaker.setBrand(brand);
        sneaker.setGender(Gender.values()[sneakerDTO.getGender()]);
        sneaker.setName(sneakerDTO.getName());
        sneaker.setDescription(sneakerDTO.getDescription());
        sneaker.setPrice(sneakerDTO.getPrice());
        sneaker.setSneakerImages(sneakerImages);
        sneaker.setSneakerSizes(sneakerSizes);
    }
}
