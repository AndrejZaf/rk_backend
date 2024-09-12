package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.repository.SneakerImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

@ExtendWith(MockitoExtension.class)
class SneakerImageServiceImplUnitTest {

    @InjectMocks
    private SneakerImageServiceImpl sneakerImageService;

    @Mock
    private SneakerImageRepository sneakerImageRepository;

    private SneakerImage sneakerImage;
    private Sneaker sneaker;

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
        sneakerImage = new SneakerImage(new byte[]{}, sneaker);
    }

//    @Test
//    void findAllBySneakerIds_returnsListOfSneakerImages() {
//        // Arrange
//        when(sneakerImageRepository.findBySneakerIdIn(sneakerIds)).thenReturn(sizes);
//
//        // Act
//        List<Double> actualSizes = sneakerSizeService.findAllDistinctSizes();
//
//        // Assert
//        assertThat(actualSizes, is(equalTo(sizes)));
//    }
//
//    @Test
//    void create_returnsSuccessfullyCreatedSneaker() {
//        // Arrange
//        Collection<SneakerSizeDTO> sneakerSizes = List.of(
//                new SneakerSizeDTO(8.5, 10L),
//                new SneakerSizeDTO(9.0, 20L));
//        Set<SneakerSize> expectedSneakerSizes = Set.of(
//                new SneakerSize(sneaker, 8.5, 10L),
//                new SneakerSize(sneaker, 9.0, 20L));
//        when(sneakerSizeRepository.saveAll(anyList())).thenReturn(expectedSneakerSizes.stream().toList());
//
//        // Act
//        Set<SneakerSize> actualSneakerSizes = sneakerSizeService.create(sneaker, sneakerSizes);
//
//        // Assert
//        verify(sneakerSizeRepository).saveAll(anyList());
//        assertThat(actualSneakerSizes, is(equalTo(expectedSneakerSizes)));
//    }
//
//    @Test
//    void delete_callsDeleteAllAndFlushOnSneakerSizeRepository() {
//        // Arrange
//        Set<SneakerSize> sneakerSizes = Set.of(
//                new SneakerSize(sneaker, 8.5, 10L),
//                new SneakerSize(sneaker, 9.0, 20L));
//
//        // Act
//        sneakerSizeService.delete(sneakerSizes);
//
//        // Assert
//        verify(sneakerSizeRepository).deleteAll(sneakerSizes);
//        verify(sneakerSizeRepository).flush();
//    }
}
