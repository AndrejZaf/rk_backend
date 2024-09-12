package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.domain.SneakerSize;
import com.rarekickz.rk_inventory_service.dto.SneakerSizeDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.repository.SneakerSizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
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
class SneakerSizeServiceImplUnitTest {

    @InjectMocks
    private SneakerSizeServiceImpl sneakerSizeService;

    @Mock
    private SneakerSizeRepository sneakerSizeRepository;

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
    }


    @Test
    void findAllDistinctSizes_returnsListWithDistinctSneakerSizes() {
        // Arrange
        List<Double> sizes = List.of(8.5, 9.0, 9.5);
        when(sneakerSizeRepository.findAllDistinctSizes()).thenReturn(sizes);

        // Act
        List<Double> actualSizes = sneakerSizeService.findAllDistinctSizes();

        // Assert
        assertThat(actualSizes, is(equalTo(sizes)));
    }

    @Test
    void create_returnsSuccessfullyCreatedSneakerSize() {
        // Arrange
        Collection<SneakerSizeDTO> sneakerSizes = List.of(
                new SneakerSizeDTO(8.5, 10L),
                new SneakerSizeDTO(9.0, 20L));
        Set<SneakerSize> expectedSneakerSizes = Set.of(
                new SneakerSize(sneaker, 8.5, 10L),
                new SneakerSize(sneaker, 9.0, 20L));
        when(sneakerSizeRepository.saveAll(anyList())).thenReturn(expectedSneakerSizes.stream().toList());

        // Act
        Set<SneakerSize> actualSneakerSizes = sneakerSizeService.create(sneaker, sneakerSizes);

        // Assert
        verify(sneakerSizeRepository).saveAll(anyList());
        assertThat(actualSneakerSizes, is(equalTo(expectedSneakerSizes)));
    }

    @Test
    void delete_successfullyDeletesSneakerSizes() {
        // Arrange
        Set<SneakerSize> sneakerSizes = Set.of(
                new SneakerSize(sneaker, 8.5, 10L),
                new SneakerSize(sneaker, 9.0, 20L));

        // Act
        sneakerSizeService.delete(sneakerSizes);

        // Assert
        verify(sneakerSizeRepository).deleteAll(sneakerSizes);
        verify(sneakerSizeRepository).flush();
    }
}
