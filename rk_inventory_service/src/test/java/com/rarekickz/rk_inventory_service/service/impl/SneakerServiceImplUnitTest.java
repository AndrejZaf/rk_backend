package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.domain.SneakerSizeId;
import com.rarekickz.rk_inventory_service.dto.ReserveSneakerDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerDTO;
import com.rarekickz.rk_inventory_service.dto.SneakerSizeDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.exception.InvalidSizeException;
import com.rarekickz.rk_inventory_service.exception.InvalidSneakerException;
import com.rarekickz.rk_inventory_service.external.ExternalOrderService;
import com.rarekickz.rk_inventory_service.repository.SneakerRepository;
import com.rarekickz.rk_inventory_service.service.BrandService;
import com.rarekickz.rk_inventory_service.service.SneakerImageService;
import com.rarekickz.rk_inventory_service.service.SneakerSizeService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SneakerServiceImplUnitTest {

    @InjectMocks
    private SneakerServiceImpl sneakerService;

    @Mock
    private SneakerRepository sneakerRepository;

    @Mock
    private SneakerImageService sneakerImageService;

    @Mock
    private SneakerSizeService sneakerSizeService;

    @Mock
    private BrandService brandService;

    @Mock
    private ExternalOrderService externalOrderService;

    private Sneaker sneaker;
    private Brand brand;

    @BeforeEach
    void setup() {
        sneaker = Sneaker.builder()
                .id(1L)
                .name("Sneaker 1")
                .price(100.0)
                .gender(Gender.MALE)
                .description("Sneaker 1 Description")
                .brand(new Brand(1L, "Brand 1", new byte[]{}))
                .sneakerImages(new HashSet<>())
                .build();
        brand = new Brand(1L, "Nike", new byte[]{});
    }

    @Test
    void findPremiumSneaker_returnsSneaker() {
        // Arrange
        sneaker.setSpecial(true);
        when(sneakerRepository.findBySpecialIsTrue()).thenReturn(Optional.of(sneaker));

        // Act
        Sneaker actualPremiumSneaker = sneakerService.findPremiumSneaker();

        // Assert
        assertThat(actualPremiumSneaker, is(equalTo(sneaker)));
    }

    @Test
    void findPremiumSneaker_throwsEntityNotFoundException() {
        // Arrange
        when(sneakerRepository.findBySpecialIsTrue()).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> sneakerService.findPremiumSneaker());
    }

    @Test
    void findMostPopularSneaker_returnsSneaker() {
        // Arrange
        when(externalOrderService.findMostPopularSneakerId()).thenReturn(1L);
        when(sneakerRepository.findByIdWithImages(1L)).thenReturn(Optional.of(sneaker));

        // Act
        Sneaker actualPremiumSneaker = sneakerService.findMostPopularSneaker();

        // Assert
        assertThat(actualPremiumSneaker, is(equalTo(sneaker)));
    }

    @Test
    void findMostPopularSneaker_throwsEntityNotFoundException() {
        // Arrange
        when(externalOrderService.findMostPopularSneakerId()).thenReturn(1L);
        when(sneakerRepository.findByIdWithImages(1L)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> sneakerService.findMostPopularSneaker());
    }

    @Test
    void findById_returnsSneaker() {
        // Arrange
        Long sneakerId = 1L;
        when(sneakerRepository.findByIdWithImages(sneakerId)).thenReturn(Optional.of(sneaker));

        // Act
        Sneaker actualSneaker = sneakerService.findById(sneakerId);

        // Assert
        assertThat(actualSneaker, is(equalTo(sneaker)));
    }

    @Test
    void findById_throwsEntityNotFoundException() {
        // Arrange
        Long sneakerId = 1L;
        when(sneakerRepository.findByIdWithImages(sneakerId)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> sneakerService.findById(sneakerId));
    }

    @Test
    void findAllByIdWithImages_returnsSneakers_whenIdsProvided() {
        // Arrange
        List<Long> sneakerIds = List.of(1L, 2L, 3L);
        List<Sneaker> expectedSneakers = List.of(
                Sneaker.builder().id(1L).name("Sneaker 1").build(),
                Sneaker.builder().id(2L).name("Sneaker 2").build(),
                Sneaker.builder().id(3L).name("Sneaker 3").build()
        );
        when(sneakerRepository.findAllByIdWithImages(sneakerIds)).thenReturn(expectedSneakers);

        // Act
        List<Sneaker> actualSneakers = sneakerService.findAllByIdWithImages(sneakerIds);

        // Assert
        assertThat(actualSneakers, is(equalTo(expectedSneakers)));
    }

    @Test
    void reserve_reservesSneakers() {
        // Arrange
        List<ReserveSneakerDTO> reservedSneakers = List.of(new ReserveSneakerDTO(1L, 10.0));
        List<Sneaker> sneakers = List.of(sneaker);
        Set<SneakerSize> sneakerSizes = Set.of(new SneakerSize(sneaker, 10.0, 2L));
        sneaker.setSneakerSizes(sneakerSizes);
        when(sneakerRepository.findAllWithSizes(anyList())).thenReturn(sneakers);

        // Act
        sneakerService.reserve(reservedSneakers);

        // Assert
        verify(sneakerRepository).findAllWithSizes(List.of(1L));
        verify(sneakerRepository).saveAll(sneakers);
    }

    @Test
    void reserve_throwsInvalidSneakerException() {
        // Arrange
        List<ReserveSneakerDTO> reservedSneakers = List.of(new ReserveSneakerDTO(1L, 10.0), new ReserveSneakerDTO(2L, 10.0));
        List<Sneaker> sneakers = List.of(sneaker);
        when(sneakerRepository.findAllWithSizes(anyList())).thenReturn(sneakers);

        // Act
        // Assert
        assertThrows(InvalidSneakerException.class, () -> sneakerService.reserve(reservedSneakers));
    }

    @Test
    void reserve_throwsInvalidSizeExceptionMissingSize() {
        // Arrange
        List<ReserveSneakerDTO> reservedSneakers = List.of(new ReserveSneakerDTO(1L, 10.0));
        List<Sneaker> sneakers = List.of(sneaker);
        Set<SneakerSize> sneakerSizes = Set.of(new SneakerSize(sneaker, 15.0, 2L));
        sneaker.setSneakerSizes(sneakerSizes);
        when(sneakerRepository.findAllWithSizes(anyList())).thenReturn(sneakers);

        // Act
        // Assert
        assertThrows(InvalidSizeException.class, () -> sneakerService.reserve(reservedSneakers));
    }

    @Test
    void reserve_throwsInvalidSizeExceptionNoQuantityAvailable() {
        // Arrange
        List<ReserveSneakerDTO> reservedSneakers = List.of(new ReserveSneakerDTO(1L, 10.0));
        List<Sneaker> sneakers = List.of(sneaker);
        Set<SneakerSize> sneakerSizes = Set.of(new SneakerSize(sneaker, 10.0, 0L));
        sneaker.setSneakerSizes(sneakerSizes);
        when(sneakerRepository.findAllWithSizes(anyList())).thenReturn(sneakers);

        // Act
        // Assert
        assertThrows(InvalidSizeException.class, () -> sneakerService.reserve(reservedSneakers));
    }

    @Test
    void testCancel_InvalidSizeException() {
        // Arrange
        ReserveSneakerDTO dto = new ReserveSneakerDTO(1L, 8.0);
        List<ReserveSneakerDTO> reservedSneakers = Collections.singletonList(dto);
        SneakerSizeId sizeId = new SneakerSizeId(1L, 9.0);
        SneakerSize sneakerSize = new SneakerSize();
        sneakerSize.setSneakerSizeId(sizeId);
        sneakerSize.setQuantity(5L);
        sneaker.setSneakerSizes(Set.of(sneakerSize));

        when(sneakerRepository.findAllWithSizes(anyList())).thenReturn(Collections.singletonList(sneaker));

        // Act
        // Assert
        assertThrows(InvalidSizeException.class, () -> sneakerService.cancel(reservedSneakers));
        verify(sneakerRepository, never()).saveAll(anyList());
    }

    @Test
    void cancel_successfullyCancelSneakerOrder() {
        // Arrange
        ReserveSneakerDTO reserveSneakerDTO = new ReserveSneakerDTO(1L, 9.0);
        SneakerSizeId sizeId = new SneakerSizeId(1L, 9.0);
        SneakerSize sneakerSize = new SneakerSize();
        sneakerSize.setSneakerSizeId(sizeId);
        sneakerSize.setQuantity(5L);
        sneaker.setSneakerSizes(Set.of(sneakerSize));
        when(sneakerRepository.findAllWithSizes(anyList())).thenReturn(List.of(sneaker));

        // Act
        sneakerService.cancel(List.of(reserveSneakerDTO));

        // Assert
        verify(sneakerRepository).saveAll(List.of(sneaker));
    }

    @Test
    void getSneakerPrices_returnsTotalPrice() {
        // Arrange
        when(sneakerRepository.findAllById(List.of(1L))).thenReturn(List.of(sneaker));

        // Act
        Double totalPrice = sneakerService.getSneakerPrices(List.of(1L));

        // Assert
        assertThat(totalPrice, is(equalTo(sneaker.getPrice())));
    }

    @Test
    void getSneakerPrices_throwsIllegalArgumentException() {
        // Arrange
        when(sneakerRepository.findAllById(List.of(1L))).thenReturn(Collections.emptyList());

        // Act
        // Assert
        assertThrows(IllegalArgumentException.class, () -> sneakerService.getSneakerPrices(List.of(1L)));
    }

    @Test
    void findAllByIds_returnsListOfSneakers() {
        // Arrange
        List<Long> sneakerIds = List.of(1L);
        List<Sneaker> sneakers = List.of(sneaker);
        when(sneakerRepository.findAllById(sneakerIds)).thenReturn(sneakers);

        // Act
        List<Sneaker> actualSneakers = sneakerService.findAllByIds(sneakerIds);

        // Assert
        assertThat(actualSneakers, is(equalTo(sneakers)));
    }

    @Test
    void findAllByPages_returnsListOfSneakers() {
        // Arrange
        int page = 0;
        int size = 10;
        List<Long> brandIds = Arrays.asList(1L, 2L);
        List<Gender> genders = Arrays.asList(Gender.MALE, Gender.FEMALE);
        List<Double> sizes = Arrays.asList(9.0, 10.0);
        String sort = "price;asc";
        String name = "Air";
        Sort sortParams = Sort.by(Sort.Order.asc("price"));
        PageRequest pageRequest = PageRequest.of(page, size, sortParams);
        List<Sneaker> sneakerList = List.of(sneaker);
        Page<Sneaker> sneakerPage = new PageImpl<>(sneakerList, pageRequest, sneakerList.size());
        SneakerImage sneakerImage = new SneakerImage();
        sneakerImage.setId(1L);
        sneakerImage.setSneaker(sneaker);
        List<SneakerImage> sneakerImages = List.of(sneakerImage);
        when(sneakerRepository.findAll((Specification<Sneaker>) any(), (PageRequest) any())).thenReturn(sneakerPage);
        List<Long> sneakerIds = List.of(1L);
        when(sneakerImageService.findAllBySneakerIds(sneakerIds)).thenReturn(sneakerImages);

        // Act
        List<Sneaker> actualSneakers = sneakerService.findAllByPages(page, size, brandIds, genders, sizes, sort, name);

        // Assert
        assertThat(actualSneakers, is(equalTo(sneakerPage.stream().toList())));
    }

    @Test
    void create_returnsNewlyCreatedSneaker() {
        // Arrange
        SneakerSizeDTO sneakerSizeDTO = new SneakerSizeDTO(10.0, 10L);
        SneakerDTO sneakerDTO = SneakerDTO.builder()
                .name("Air Max")
                .brandId(brand.getId())
                .description("A comfortable running shoe.")
                .price(150.0)
                .images(List.of("image1.jpg"))
                .sizes(List.of(sneakerSizeDTO))
                .gender(0)
                .build();
        SneakerImage sneakerImage = new SneakerImage("image1.jpg".getBytes(), sneaker);
        Set<SneakerImage> sneakerImages = Set.of(sneakerImage);
        SneakerSize sneakerSize = new SneakerSize(sneaker, 10.0, 10L);
        Set<SneakerSize> sneakerSizes = Set.of(sneakerSize);
        when(sneakerRepository.save(any())).thenReturn(sneaker);
        when(brandService.findById(brand.getId())).thenReturn(brand);
        when(sneakerImageService.create(sneakerDTO.getImages(), sneaker)).thenReturn(sneakerImages);
        when(sneakerSizeService.create(sneaker, sneakerDTO.getSizes())).thenReturn(sneakerSizes);

        // Act
        Sneaker actualSneaker = sneakerService.create(sneakerDTO);

        // Assert
        assertThat(sneaker, is(equalTo(actualSneaker)));
    }

    @Test
    void update_returnsUpdatedSneaker() {
        // Arrange
        SneakerSizeDTO sneakerSizeDTO = new SneakerSizeDTO(10.0, 10L);
        SneakerDTO sneakerDTO = SneakerDTO.builder()
                .id(1L)
                .name("Air Max")
                .brandId(brand.getId())
                .description("A comfortable running shoe.")
                .price(150.0)
                .images(List.of("image1.jpg"))
                .sizes(List.of(sneakerSizeDTO))
                .gender(0)
                .build();

        SneakerImage sneakerImage = new SneakerImage("image1.jpg".getBytes(), sneaker);
        Set<SneakerImage> sneakerImages = Set.of(sneakerImage);
        SneakerSize sneakerSize = new SneakerSize(sneaker, 10.0, 10L);
        Set<SneakerSize> sneakerSizes = Set.of(sneakerSize);
        when(brandService.findById(brand.getId())).thenReturn(brand);
        when(sneakerRepository.findById(sneakerDTO.getId())).thenReturn(Optional.of(sneaker));
        when(sneakerImageService.create(sneakerDTO.getImages(), sneaker)).thenReturn(sneakerImages);
        when(sneakerSizeService.create(sneaker, sneakerDTO.getSizes())).thenReturn(sneakerSizes);
        when(sneakerRepository.save(any())).thenReturn(sneaker);

        // Act
        Sneaker actualSneaker = sneakerService.update(sneakerDTO);

        // Assert
        assertThat(sneaker, is(equalTo(actualSneaker)));
    }

    @Test
    void update_throwsEntityNotFoundException() {
        // Arrange
        SneakerSizeDTO sneakerSizeDTO = new SneakerSizeDTO(10.0, 10L);
        SneakerDTO sneakerDTO = SneakerDTO.builder()
                .id(1L)
                .name("Air Max")
                .brandId(brand.getId())
                .description("A comfortable running shoe.")
                .price(150.0)
                .images(List.of("image1.jpg"))
                .sizes(List.of(sneakerSizeDTO))
                .gender(0)
                .build();
        when(brandService.findById(brand.getId())).thenReturn(brand);
        when(sneakerRepository.findById(sneakerDTO.getId())).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> sneakerService.update(sneakerDTO));
    }

    @Test
    void deleteById_throwsEntityNotFoundException() {
        // Arrange
        when(sneakerRepository.findById(sneaker.getId())).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> sneakerService.deleteById(sneaker.getId()));
    }

    @Test
    void deleteById_successfullyDeletesSneaker() {
        // Arrange
        when(sneakerRepository.findById(sneaker.getId())).thenReturn(Optional.of(sneaker));

        // Act
        sneakerService.deleteById(sneaker.getId());

        // Assert
        verify(sneakerSizeService).delete(sneaker.getSneakerSizes());
        verify(sneakerImageService).delete(sneaker.getSneakerImages());
        verify(sneakerRepository).delete(sneaker);
    }

    @Test
    void findAll_returnsListOfSneakers() {
        // Arrange
        List<Sneaker> sneakers = List.of(sneaker);
        when(sneakerRepository.findAllSneakersWithImages()).thenReturn(sneakers);

        // Act
        List<Sneaker> actualSneakers = sneakerService.findAll();

        // Assert
        assertThat(actualSneakers, is(equalTo(sneakers)));
    }

    @Test
    void premium_sneakerIsAlreadyMarkedAsPremium() {
        // Arrange
        sneaker.setSpecial(true);
        when(sneakerRepository.findById(sneaker.getId())).thenReturn(Optional.of(sneaker));

        // Act
        sneakerService.premium(sneaker.getId());

        // Assert
        verify(sneakerRepository, never()).findBySpecialIsTrue();
    }

    @Test
    void premium_throwsEntityNotFoundException() {
        // Arrange
        when(sneakerRepository.findById(sneaker.getId())).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> sneakerService.premium(sneaker.getId()));
    }

    @Test
    void premium_makesANewSpecialSneakerSinceThereAreNoSpecialSneakers() {
        // Arrange
        sneaker.setSpecial(false);
        when(sneakerRepository.findById(sneaker.getId())).thenReturn(Optional.of(sneaker));
        when(sneakerRepository.findBySpecialIsTrue()).thenReturn(Optional.empty());

        // Act
        sneakerService.premium(sneaker.getId());

        // Assert
        verify(sneakerRepository).findBySpecialIsTrue();
        verify(sneakerRepository).save(sneaker);
    }

    @Test
    void premium_replacesNewSpecialSneaker() {
        // Arrange
        sneaker.setSpecial(false);
        when(sneakerRepository.findById(sneaker.getId())).thenReturn(Optional.of(sneaker));
        when(sneakerRepository.findBySpecialIsTrue()).thenReturn(Optional.of(sneaker));

        // Act
        sneakerService.premium(sneaker.getId());

        // Assert
        verify(sneakerRepository).findBySpecialIsTrue();
        verify(sneakerRepository).saveAll(anyList());
    }
}
