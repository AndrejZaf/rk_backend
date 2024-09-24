package com.rarekickz.rk_inventory_service.external;

import com.google.protobuf.Empty;
import com.google.rpc.Code;
import com.google.rpc.Status;
import com.rarekickz.proto.lib.OrderTotalPriceResponse;
import com.rarekickz.proto.lib.ReserveSneakersRequest;
import com.rarekickz.proto.lib.SneakerDetails;
import com.rarekickz.proto.lib.SneakerDetailsResponse;
import com.rarekickz.proto.lib.SneakerIdsRequest;
import com.rarekickz.proto.lib.SneakerServiceGrpc;
import com.rarekickz.rk_inventory_service.domain.Sneaker;
import com.rarekickz.rk_inventory_service.dto.ReserveSneakerDTO;
import com.rarekickz.rk_inventory_service.exception.InvalidSizeException;
import com.rarekickz.rk_inventory_service.exception.InvalidSneakerException;
import com.rarekickz.rk_inventory_service.service.SneakerService;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;

import static com.rarekickz.rk_inventory_service.external.converter.ExternalSneakerConverter.convertToReserveSneakerDTOs;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ExternalSneakerService extends SneakerServiceGrpc.SneakerServiceImplBase {

    private final SneakerService sneakerService;

    @Override
    public void reserve(final ReserveSneakersRequest request, final StreamObserver<Empty> responseObserver) {
        log.debug("Received request to reserve sneakers");
        final List<ReserveSneakerDTO> sneakersToBeReserved = convertToReserveSneakerDTOs(request);
        try {
            sneakerService.reserve(sneakersToBeReserved);
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();
        } catch (InvalidSizeException ex) {
            final Status status = Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT_VALUE)
                    .setMessage("The selected size is not available")
                    .build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(status));
        } catch (InvalidSneakerException ex) {
            final Status status = Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT_VALUE)
                    .setMessage("One of the selected sneaker is not available")
                    .build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(status));
        }
    }

    @Override
    public void cancelReservation(final ReserveSneakersRequest request, final StreamObserver<Empty> responseObserver) {
        log.debug("Received request to cancel reservation");
        final List<ReserveSneakerDTO> sneakersToBeCanceled = convertToReserveSneakerDTOs(request);
        sneakerService.cancel(sneakersToBeCanceled);
        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getSneakerPrice(final SneakerIdsRequest request,
                                final StreamObserver<OrderTotalPriceResponse> responseObserver) {
        log.debug("Received request to get sneaker price");
        final Double totalPrice = sneakerService.getSneakerPrices(request.getSneakerIdList());
        final OrderTotalPriceResponse totalPriceResponse = OrderTotalPriceResponse.newBuilder()
                .setPrice(totalPrice)
                .build();
        responseObserver.onNext(totalPriceResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getSneakerDetails(final SneakerIdsRequest request,
                                  final StreamObserver<SneakerDetailsResponse> responseObserver) {
        log.debug("Received request to get sneaker details");
        final List<Sneaker> sneakers = sneakerService.findAllByIds(request.getSneakerIdList());
        final List<SneakerDetails> sneakerDetails = sneakers.stream()
                .map(sneaker -> SneakerDetails.newBuilder()
                        .setPrice(sneaker.getPrice())
                        .setName(sneaker.getName())
                        .setId(sneaker.getId())
                        .build())
                .toList();
        final SneakerDetailsResponse sneakerDetailsResponse = SneakerDetailsResponse.newBuilder()
                .addAllSneakerDetails(sneakerDetails)
                .build();
        responseObserver.onNext(sneakerDetailsResponse);
        responseObserver.onCompleted();
    }
}
