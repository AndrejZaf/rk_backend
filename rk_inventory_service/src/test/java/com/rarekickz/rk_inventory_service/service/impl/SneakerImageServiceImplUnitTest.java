package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerImage;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.repository.SneakerImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void findAllBySneakerIds_returnsListOfSneakerImages() {
        // Arrange
        List<Long> sneakerIds = List.of(sneaker.getId());
        List<SneakerImage> sneakerImages = List.of(sneakerImage);
        when(sneakerImageRepository.findBySneakerIdIn(sneakerIds)).thenReturn(sneakerImages);

        // Act
        List<SneakerImage> actualImages = sneakerImageService.findAllBySneakerIds(sneakerIds);

        // Assert
        assertThat(actualImages, is(equalTo(sneakerImages)));
    }

    @Test
    void create_returnsSuccessfullyCreatedSneaker() {
        // Arrange
        Set<SneakerImage> sneakerImages = Set.of(sneakerImage);
        when(sneakerImageRepository.saveAll(anyList())).thenReturn(sneakerImages.stream().toList());

        // Act
        Set<SneakerImage> actualSneakerImages = sneakerImageService.create(List.of(""), sneaker);

        // Assert
        verify(sneakerImageRepository).saveAll(anyList());
        assertThat(actualSneakerImages, is(equalTo(sneakerImages)));
    }

    @Test
    void delete_callsDeleteAllAndFlushOnSneakerSizeRepository() {
        // Arrange
        List<SneakerImage> sneakerImages = List.of(sneakerImage);
        // Act
        sneakerImageService.delete(sneakerImages);

        // Assert
        verify(sneakerImageRepository).deleteAll(sneakerImages);
        verify(sneakerImageRepository).flush();
    }
}
