package com.rarekickz.rk_inventory_service.service.impl;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.exception.BrandNotFoundException;
import com.rarekickz.rk_inventory_service.repository.BrandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplUnitTest {

    @InjectMocks
    private BrandServiceImpl brandService;

    @Mock
    private BrandRepository brandRepository;

    @Test
    void findById_shouldThrowBrandNotFoundException() {
        // Arrange
        Long invalidBrandId = 999L;
        when(brandRepository.findById(invalidBrandId)).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(BrandNotFoundException.class, () -> brandService.findById(invalidBrandId));
    }

    @Test
    void findById_returnsBrand() {
        // Arrange
        Brand brand = new Brand(999L, "Brand A", new byte[]{});
        when(brandRepository.findById(brand.getId())).thenReturn(Optional.of(brand));

        // Act
        Brand actualBrand = brandService.findById(brand.getId());

        // Assert
        assertThat(actualBrand, is(equalTo(brand)));
    }

    @Test
    void findAll_returnsBrandsSortedByNameAsc() {
        // Arrange
        Brand brand1 = new Brand(1L, "Brand A", new byte[]{});
        Brand brand2 = new Brand(2L, "Brand B", new byte[]{});
        List<Brand> expectedBrands = List.of(brand1, brand2);
        when(brandRepository.findAllByOrderByNameAsc()).thenReturn(expectedBrands);

        // Act
        List<Brand> actualBrands = brandService.findAll();

        // Assert
        verify(brandRepository).findAllByOrderByNameAsc();
        assertThat(actualBrands, is(equalTo(expectedBrands)));
    }
}