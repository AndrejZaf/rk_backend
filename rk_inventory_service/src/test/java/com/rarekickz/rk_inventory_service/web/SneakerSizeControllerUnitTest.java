package com.rarekickz.rk_inventory_service.web;

import com.rarekickz.rk_inventory_service.service.SneakerSizeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SneakerSizeControllerUnitTest {

    @InjectMocks
    private SneakerSizeController sneakerSizeController;

    @Mock
    private SneakerSizeService sneakerSizeService;

    @Test
    void getSneakerSizes_returnsListOfSizes() {
        // Arrange
        List<Double> sizes = List.of(8.5);
        when(sneakerSizeService.findAllDistinctSizes()).thenReturn(sizes);

        // Act
        ResponseEntity<List<Double>> actualSneakerSizesResponse = sneakerSizeController.getSneakerSizes();

        // Assert
        verify(sneakerSizeService).findAllDistinctSizes();
        assertThat(actualSneakerSizesResponse.getStatusCode(), is(HttpStatus.OK));
        assertThat(actualSneakerSizesResponse.getBody(), is(sizes));
    }
}
