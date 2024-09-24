package rarekickz.rk_order_service.external.impl;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.Brand;
import com.rarekickz.proto.lib.BrandServiceGrpc;
import com.rarekickz.proto.lib.BrandsResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rarekickz.rk_order_service.dto.BrandDTO;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalBrandServiceImplUnitTest {

    @InjectMocks
    private ExternalBrandServiceImpl externalBrandService;

    @Mock
    private BrandServiceGrpc.BrandServiceBlockingStub brandServiceBlockingStub;

    @Test
    void getAllBrands_successfullyReturnsMapOfBrands() {
        // Arrange
        Brand brand = Brand.newBuilder()
                .setId(1L)
                .setName("Test")
                .build();
        BrandsResponse brandsResponse = BrandsResponse.newBuilder().addBrands(brand).build();
        when(brandServiceBlockingStub.getAllBrands(Empty.getDefaultInstance())).thenReturn(brandsResponse);

        // Act
        Map<Long, BrandDTO> brandIdToBrandDTOMap = externalBrandService.getAllBrands();

        // Assert
        assertThat(brandIdToBrandDTOMap.get(1L), notNullValue());
        verify(brandServiceBlockingStub).getAllBrands(any());
    }
}
