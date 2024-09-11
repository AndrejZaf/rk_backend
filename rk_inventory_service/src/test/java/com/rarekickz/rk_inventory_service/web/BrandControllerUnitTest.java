package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.dto.BrandDTO;
import com.rarekickz.rk_inventory_service.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.rarekickz.rk_inventory_service.converter.BrandConverter.convertToBrandDTO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BrandControllerUnitTest {

    @InjectMocks
    private BrandController brandController;

    @Mock
    private BrandService brandService;

    @Test
    void getAllBrands_returnsListOfBrands() {
        // Arrange
        final Brand brand = new Brand(1L, "Brand 1", new byte[]{});
        final BrandDTO brandDTO = convertToBrandDTO(brand);
        final List<Brand> brands = List.of(brand);
        when(brandService.findAll()).thenReturn(brands);

        // Act
        final ResponseEntity<List<BrandDTO>> actualBrandsResponse = brandController.getAllBrands();

        // Assert
        verify(brandService).findAll();
        assertThat(actualBrandsResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualBrandsResponse.getBody(), is(List.of(brandDTO)));
    }
}
