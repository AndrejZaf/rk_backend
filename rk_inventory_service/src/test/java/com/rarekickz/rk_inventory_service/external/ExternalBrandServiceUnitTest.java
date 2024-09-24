package com.rarekickz.rk_inventory_service.external;

import com.google.protobuf.Empty;
import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.service.BrandService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalBrandServiceUnitTest {

    @InjectMocks
    private ExternalBrandService externalBrandService;

    @Mock
    private StreamObserver responseObserver;

    @Mock
    private BrandService brandService;

    @Test
    void getAllBrands_successfullySendBrands() {
        // Arrange
        Brand brand = new Brand(1L, "Nike", null);
        when(brandService.findAll()).thenReturn(List.of(brand));

        // Act
        externalBrandService.getAllBrands(Empty.getDefaultInstance(), responseObserver);

        // Assert
        verify(brandService).findAll();
        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }
}
