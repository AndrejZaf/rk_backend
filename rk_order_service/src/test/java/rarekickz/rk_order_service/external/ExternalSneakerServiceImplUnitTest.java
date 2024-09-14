package rarekickz.rk_order_service.external;

import com.rarekickz.proto.lib.ExtendedSneakerDetails;
import com.rarekickz.proto.lib.ExtendedSneakerDetailsResponse;
import com.rarekickz.proto.lib.OrderTotalPriceResponse;
import com.rarekickz.proto.lib.SneakerDetails;
import com.rarekickz.proto.lib.SneakerDetailsResponse;
import com.rarekickz.proto.lib.SneakerServiceGrpc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.ExtendedSneakerDetailsDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.external.impl.ExternalSneakerServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static rarekickz.rk_order_service.external.converter.SneakerDetailsConverter.convertToExtendedSneakerDTOList;
import static rarekickz.rk_order_service.external.converter.SneakerDetailsConverter.convertToExtendedSneakerDetailsDTOList;

@ExtendWith(MockitoExtension.class)
class ExternalSneakerServiceImplUnitTest {

    @InjectMocks
    private ExternalSneakerServiceImpl externalSneakerService;

    @Mock
    private SneakerServiceGrpc.SneakerServiceBlockingStub sneakerServiceBlockingStub;

    @Test
    void reserve_successfullySendsRequestToReserveSneakers() {
        // Arrange
        SneakerDTO sneakerDTO = new SneakerDTO(1L, 15.0);

        // Act
        externalSneakerService.reserve(List.of(sneakerDTO));

        // Assert
        verify(sneakerServiceBlockingStub).reserve(any());
    }

    @Test
    void cancel_successfullySendsRequestToCancelSneakers() {
        // Arrange
        SneakerDTO sneakerDTO = new SneakerDTO(1L, 15.0);

        // Act
        externalSneakerService.cancel(List.of(sneakerDTO));

        // Assert
        verify(sneakerServiceBlockingStub).cancelReservation(any());
    }

    @Test
    void getTotalPrice_returnsTotalSneakerPrice() {
        // Arrange
        List<Long> sneakerIds = List.of(1L);
        double expectedPrice = 150.0;
        OrderTotalPriceResponse orderTotalPriceResponse = OrderTotalPriceResponse.newBuilder()
                .setPrice(expectedPrice)
                .build();
        when(sneakerServiceBlockingStub.getSneakerPrice(any())).thenReturn(orderTotalPriceResponse);

        // Act
        Double actualTotalPrice = externalSneakerService.getTotalPrice(sneakerIds);

        // Assert
        assertThat(actualTotalPrice, is(equalTo(expectedPrice)));
    }

    @Test
    void getSneakerDetails_returnsListOfSneakers() {
        // Arrange
        List<Long> sneakerIds = List.of(1L);
        SneakerDetails sneakerDetails = SneakerDetails.newBuilder()
                .setId(1L)
                .setPrice(150.0)
                .setName("Test")
                .build();
        SneakerDetailsResponse sneakerDetailsResponse = SneakerDetailsResponse.newBuilder()
                .addSneakerDetails(sneakerDetails)
                .build();
        when(sneakerServiceBlockingStub.getSneakerDetails(any())).thenReturn(sneakerDetailsResponse);
        List<ExtendedSneakerDTO> extendedSneakerDTOs = convertToExtendedSneakerDTOList(sneakerDetailsResponse.getSneakerDetailsList());

        // Act
        List<ExtendedSneakerDTO> actualSneakerDetails = externalSneakerService.getSneakerDetails(sneakerIds);

        // Assert
        assertThat(actualSneakerDetails, hasSize(1));
        assertThat(actualSneakerDetails, is(equalTo(extendedSneakerDTOs)));
    }


    @Test
    void getExtendedSneakerDetails() {
        // Arrange
        List<Long> sneakerIds = List.of(1L);
        ExtendedSneakerDetails sneakerDetails = ExtendedSneakerDetails.newBuilder()
                .setId(1L)
                .setPrice(150.0)
                .setName("Test")
                .build();
        ExtendedSneakerDetailsResponse sneakerDetailsResponse = ExtendedSneakerDetailsResponse.newBuilder()
                .addSneakerDetails(sneakerDetails)
                .build();
        when(sneakerServiceBlockingStub.getExtendedSneakerDetails(any())).thenReturn(sneakerDetailsResponse);
        List<ExtendedSneakerDetailsDTO> extendedSneakerDTOs = convertToExtendedSneakerDetailsDTOList(sneakerDetailsResponse.getSneakerDetailsList());

        // Act
        List<ExtendedSneakerDetailsDTO> actualSneakerExtendedDetails = externalSneakerService.getExtendedSneakerDetails(sneakerIds);

        // Assert
        assertThat(actualSneakerExtendedDetails, hasSize(1));
        assertThat(actualSneakerExtendedDetails, is(equalTo(extendedSneakerDTOs)));
    }
}
