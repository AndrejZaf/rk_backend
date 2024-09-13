package com.rarekickz.rk_inventory_service.external;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.ExtendedSneakerDetails;
import com.rarekickz.proto.lib.ExtendedSneakerDetailsResponse;
import com.rarekickz.proto.lib.OrderTotalPriceResponse;
import com.rarekickz.proto.lib.ReserveSneakersRequest;
import com.rarekickz.proto.lib.SneakerDetails;
import com.rarekickz.proto.lib.SneakerDetailsResponse;
import com.rarekickz.proto.lib.SneakerIdsRequest;
import com.rarekickz.proto.lib.SneakerRequest;
import com.rarekickz.rk_inventory_service.domain.Brand;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.dto.ReserveSneakerDTO;
import com.rarekickz.rk_inventory_service.enums.Gender;
import com.rarekickz.rk_inventory_service.exception.InvalidSizeException;
import com.rarekickz.rk_inventory_service.exception.InvalidSneakerException;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalSneakerServiceUnitTest {

    @InjectMocks
    private ExternalSneakerService externalSneakerService;

    @Mock
    private SneakerService sneakerService;

    @Mock
    private StreamObserver responseObserver;

    private ReserveSneakersRequest request;
    private List<ReserveSneakerDTO> sneakersToBeReserved;
    private SneakerIdsRequest sneakerIdsRequest;
    private Sneaker sneaker;

    @BeforeEach
    void setup() {
        request = ReserveSneakersRequest.newBuilder()
                .addSneakers(SneakerRequest.newBuilder()
                        .setSneakerId(1L)
                        .setSneakerSize(9.0)
                        .build())
                .build();
        sneakersToBeReserved = List.of(new ReserveSneakerDTO(1L, 9.0));
        sneakerIdsRequest = SneakerIdsRequest.newBuilder()
                .addSneakerId(1L)
                .build();
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
    void reserve_successfullyReservesSneakers() {
        // Arrange
        // Act
        externalSneakerService.reserve(request, responseObserver);

        // Assert
        verify(sneakerService).reserve(sneakersToBeReserved);
        verify(responseObserver).onNext(Empty.newBuilder().build());
        verify(responseObserver).onCompleted();
    }

    @Test
    void reserve_throwsInvalidSneakerException() {
        // Arrange
        doThrow(new InvalidSneakerException("The selected sneaker is not available")).when(sneakerService).reserve(sneakersToBeReserved);

        // Act
        externalSneakerService.reserve(request, responseObserver);

        // Assert
        verify(sneakerService).reserve(sneakersToBeReserved);
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
        verify(responseObserver).onError(any());
    }

    @Test
    void reserve_throwsInvalidSizeException() {
        // Arrange
        doThrow(new InvalidSizeException("The selected size is not available")).when(sneakerService).reserve(sneakersToBeReserved);

        // Act
        externalSneakerService.reserve(request, responseObserver);

        // Assert
        verify(sneakerService).reserve(sneakersToBeReserved);
        verify(responseObserver, never()).onNext(any());
        verify(responseObserver, never()).onCompleted();
        verify(responseObserver).onError(any());
    }

    @Test
    void cancelReservation_successullyCancelsActiveReservation() {
        // Arrange
        // Act
        externalSneakerService.cancelReservation(request, responseObserver);

        // Assert
        verify(sneakerService).cancel(sneakersToBeReserved);
        verify(responseObserver).onNext(Empty.newBuilder().build());
        verify(responseObserver).onCompleted();
    }

    @Test
    void getSneakerPrice_returnsTotalPriceForSelectedSneakers() {
        // Arrange
        when(sneakerService.getSneakerPrices(List.of(1L))).thenReturn(150.0);
        OrderTotalPriceResponse totalPriceResponse = OrderTotalPriceResponse.newBuilder()
                .setPrice(150.0)
                .build();

        // Act
        externalSneakerService.getSneakerPrice(sneakerIdsRequest, responseObserver);

        // Assert
        verify(sneakerService).getSneakerPrices(List.of(1L));
        verify(responseObserver).onNext(totalPriceResponse);
        verify(responseObserver).onCompleted();
    }

    @Test
    void getSneakerDetails_returnsSneakerDetailsForSelectedSneakers() {
        // Arrange
        SneakerDetails sneakerDetails = SneakerDetails.newBuilder()
                .setPrice(sneaker.getPrice())
                .setName(sneaker.getName())
                .setId(sneaker.getId())
                .build();
        SneakerDetailsResponse sneakerDetailsResponse = SneakerDetailsResponse.newBuilder()
                .addSneakerDetails(sneakerDetails)
                .build();
        when(sneakerService.findAllByIds(List.of(1L))).thenReturn(List.of(sneaker));

        // Act
        externalSneakerService.getSneakerDetails(sneakerIdsRequest, responseObserver);

        // Assert
        verify(responseObserver).onNext(sneakerDetailsResponse);
        verify(responseObserver).onCompleted();
    }

    @Test
    void getExtendedSneakerDetails_returnsSneakerDetailsForSelectedSneakers() {
        // Arrange
        ExtendedSneakerDetails sneakerDetails = ExtendedSneakerDetails.newBuilder()
                .setPrice(sneaker.getPrice())
                .setName(sneaker.getName())
                .setId(sneaker.getId())
                .setBrandName(sneaker.getBrand().getName())
                .build();
        ExtendedSneakerDetailsResponse sneakerDetailsResponse = ExtendedSneakerDetailsResponse.newBuilder()
                .addSneakerDetails(sneakerDetails)
                .build();
        when(sneakerService.findAllByIds(List.of(1L))).thenReturn(List.of(sneaker));

        // Act
        externalSneakerService.getExtendedSneakerDetails(sneakerIdsRequest, responseObserver);

        // Assert
        verify(responseObserver).onNext(sneakerDetailsResponse);
        verify(responseObserver).onCompleted();
    }
}
