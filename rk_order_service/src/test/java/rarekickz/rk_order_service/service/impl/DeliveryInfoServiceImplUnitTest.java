package rarekickz.rk_order_service.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;
import rarekickz.rk_order_service.repository.DeliveryInfoRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rarekickz.rk_order_service.converter.DeliveryInfoConverter.toDeliveryInfoDTO;

@ExtendWith(MockitoExtension.class)
class DeliveryInfoServiceImplUnitTest {

    @InjectMocks
    private DeliveryInfoServiceImpl deliveryInfoService;

    @Mock
    private DeliveryInfoRepository deliveryInfoRepository;

    @Test
    void save_successfullySavesDeliveryInfo() {
        // Arrange
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .id(1L)
                .firstName("Andrej")
                .lastName("Zafirovski")
                .email("andrei.zafirovski@example.com")
                .phoneNumber("1234567890")
                .city("Skopje")
                .country("Macedonia")
                .street("Somewhere")
                .build();
        DeliveryInfoDTO deliveryInfoDTO = toDeliveryInfoDTO(deliveryInfo);
        when(deliveryInfoRepository.save(any())).thenReturn(deliveryInfo);

        // Act
        DeliveryInfo actualDeliveryInfo = deliveryInfoService.save(deliveryInfoDTO);

        // Assert
        assertThat(actualDeliveryInfo, is(equalTo(deliveryInfo)));
    }
}
