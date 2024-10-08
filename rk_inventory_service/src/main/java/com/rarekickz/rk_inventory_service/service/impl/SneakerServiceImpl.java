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
import com.rarekickz.rk_inventory_service.exception.SneakerNotFoundException;
import com.rarekickz.rk_inventory_service.external.ExternalOrderService;
import com.rarekickz.rk_inventory_service.repository.SneakerRepository;
import com.rarekickz.rk_inventory_service.service.BrandService;
import com.rarekickz.rk_inventory_service.service.SneakerImageService;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import com.rarekickz.rk_inventory_service.service.SneakerSizeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.rarekickz.rk_inventory_service.specification.SneakerSpecification.createSneakerSpecification;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class SneakerServiceImpl implements SneakerService {

    private static final String THE_SELECTED_SIZE_IS_NOT_AVAILABLE = "The selected size is not available";

    private final SneakerRepository sneakerRepository;
    private final SneakerImageService sneakerImageService;
    private final SneakerSizeService sneakerSizeService;
    private final BrandService brandService;
    private final ExternalOrderService externalOrderService;

    @Override
    @Transactional
    public Sneaker findPremiumSneaker() {
        log.debug("Retrieving premium sneaker from database");
        return sneakerRepository.findBySpecialIsTrue().orElseThrow(SneakerNotFoundException::new);
    }

    @Override
    @Transactional
    public Sneaker findMostPopularSneaker() {
        log.debug("Retrieving most popular sneaker from database");
        final Long sneakerId = externalOrderService.findMostPopularSneakerId();
        return sneakerRepository.findByIdWithImages(sneakerId).orElseThrow(SneakerNotFoundException::new);
    }

    @Override
    @Transactional
    public Sneaker findById(final Long sneakerId) {
        log.debug("Retrieving sneaker by ID: [{}]", sneakerId);
        return sneakerRepository.findByIdWithImages(sneakerId).orElseThrow(SneakerNotFoundException::new);
    }

    @Override
    @Transactional
    public List<Sneaker> findAllByIdWithImages(final List<Long> sneakerIds) {
        log.debug("Retrieving sneakers with images by IDs: [{}]", sneakerIds);
        return sneakerRepository.findAllByIdWithImages(sneakerIds);
    }

    @Override
    public void reserve(final Collection<ReserveSneakerDTO> reservedSneakers) {
        final List<Long> sneakerIds = reservedSneakers.stream()
                .map(ReserveSneakerDTO::getSneakerId)
                .toList();
        log.debug("Reserving sneakers by IDs: [{}]", sneakerIds);
        final Map<Long, ReserveSneakerDTO> sneakerIdToSizes = reservedSneakers.stream()
                .collect(Collectors.toMap(ReserveSneakerDTO::getSneakerId, Function.identity()));
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
                    .findFirst().orElseThrow(() -> new InvalidSizeException(THE_SELECTED_SIZE_IS_NOT_AVAILABLE));
            if (sneakerSizeFromDb.getQuantity().equals(0L)) {
                throw new InvalidSizeException(THE_SELECTED_SIZE_IS_NOT_AVAILABLE);
            }

            sneakerSizeFromDb.setQuantity(sneakerSizeFromDb.getQuantity() - 1);
        });
        sneakerRepository.saveAll(sneakers);
    }

    @Override
    public void cancel(final Collection<ReserveSneakerDTO> reservedSneakers) {
        final List<Long> sneakerIds = reservedSneakers.stream()
                .map(ReserveSneakerDTO::getSneakerId)
                .toList();
        log.debug("Cancelling order for sneakers with IDs: [{}]", sneakerIds);
        final Map<Long, ReserveSneakerDTO> sneakerIdToSizes = reservedSneakers.stream()
                .collect(Collectors.toMap(ReserveSneakerDTO::getSneakerId, Function.identity()));
        final List<Sneaker> sneakers = sneakerRepository.findAllWithSizes(sneakerIds);
        sneakers.forEach(sneaker -> {
            final Double size = sneakerIdToSizes.get(sneaker.getId()).getSize();
            final SneakerSize sneakerSizeFromDb = sneaker.getSneakerSizes().stream()
                    .filter(sneakerSize -> sneakerSize.getSneakerSizeId().getSize().equals(size))
                    .findFirst().orElseThrow(() -> new InvalidSizeException(THE_SELECTED_SIZE_IS_NOT_AVAILABLE));
            sneakerSizeFromDb.setQuantity(sneakerSizeFromDb.getQuantity() + 1);
        });
        sneakerRepository.saveAll(sneakers);
    }

    @Override
    public Double getSneakerPrices(final Collection<Long> sneakerIds) {
        log.debug("Retrieving sneakers prices by sneaker IDs: [{}]", sneakerIds);
        final List<Sneaker> sneakers = sneakerRepository.findAllById(sneakerIds);
        return sneakers.stream()
                .map(Sneaker::getPrice)
                .reduce(Double::sum)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    public List<Sneaker> findAllByIds(final Collection<Long> sneakerIds) {
        log.debug("Retrieving sneakers by IDs: [{}]", sneakerIds);
        return sneakerRepository.findAllById(sneakerIds);
    }

    @Override
    @Transactional
    public List<Sneaker> findAllByPages(final int page, final int size, final List<Long> brandIds, final List<Gender> genders,
                                        final List<Double> sizes, final String sort, final String name) {
        log.debug("Retrieving sneakers from database by page: [{}], size: [{}], brandIds: [{}], genders: [{}], sizes: [{}]",
                page, size, brandIds, genders, sizes);
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(parseSortParams(sort)));
        final Specification<Sneaker> sneakerSpecification = createSneakerSpecification(brandIds, genders, sizes, name);
        final Page<Sneaker> sneakers = sneakerRepository.findAll(sneakerSpecification, pageRequest);
        final List<Long> sneakerIds = sneakers.stream().map(Sneaker::getId)
                .toList();
        final List<SneakerImage> sneakerImages = sneakerImageService.findAllBySneakerIds(sneakerIds);
        final Map<Long, Set<SneakerImage>> sneakerIdToSneakerImages = sneakerImages.stream()
                .collect(groupingBy(sneakerImage -> sneakerImage.getSneaker().getId(), toSet()));
        sneakers.stream().forEach(sneaker ->
                sneaker.setSneakerImages(sneakerIdToSneakerImages.getOrDefault(sneaker.getId(), Collections.emptySet())));
        return sneakers.toList();
    }

    @Override
    @Transactional
    public Sneaker create(final SneakerDTO sneakerDTO) {
        log.debug("Creating a new sneaker with name: [{}]", sneakerDTO.getName());
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
        log.debug("Updating sneaker with ID [{}]", sneakerDTO.getId());
        final Brand brand = brandService.findById(sneakerDTO.getBrandId());
        final Sneaker sneaker = sneakerRepository.findById(sneakerDTO.getId()).orElseThrow(SneakerNotFoundException::new);
        sneakerImageService.delete(sneaker.getSneakerImages());
        sneakerSizeService.delete(sneaker.getSneakerSizes());
        final Set<SneakerImage> sneakerImages = sneakerImageService.create(sneakerDTO.getImages(), sneaker);
        final Set<SneakerSize> sneakerSizes = sneakerSizeService.create(sneaker, sneakerDTO.getSizes());
        updateSneakerProperties(sneakerDTO, brand, sneaker, sneakerImages, sneakerSizes);
        return sneakerRepository.save(sneaker);
    }

    @Override
    @Transactional
    public void deleteById(final Long sneakerId) {
        log.debug("Deleting sneaker with ID [{}]", sneakerId);
        final Sneaker sneaker = sneakerRepository.findById(sneakerId).orElseThrow(SneakerNotFoundException::new);
        sneakerSizeService.delete(sneaker.getSneakerSizes());
        sneakerImageService.delete(sneaker.getSneakerImages());
        sneakerRepository.delete(sneaker);
    }

    @Override
    public void premium(final Long sneakerId) {
        log.debug("Setting sneaker with ID [{}] as premium", sneakerId);
        final Sneaker newSpecialSneaker = sneakerRepository.findById(sneakerId).orElseThrow(SneakerNotFoundException::new);
        if (newSpecialSneaker.isSpecial()) {
            return;
        }

        newSpecialSneaker.setSpecial(true);
        final Optional<Sneaker> previousSpecialSneaker = sneakerRepository.findBySpecialIsTrue();
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
        log.debug("Retrieving all sneakers with their images from the database");
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

    private Sort.Order parseSortParams(final String sortParams) {
        final String[] split = sortParams.split(";");
        return new Sort.Order(Sort.Direction.fromString(split[1]), split[0]);
    }
}
