package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.ExtendedSneakerDetailsDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.external.ExternalSneakerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static rarekickz.rk_order_service.external.converter.SneakerDetailsConverter.convertToExtendedSneakerDTOList;
import static rarekickz.rk_order_service.external.converter.SneakerDetailsConverter.convertToExtendedSneakerDetailsDTOList;

@Service
public class ExternalSneakerServiceImpl implements ExternalSneakerService {

    @GrpcClient("sneakerService")
    private SneakerServiceGrpc.SneakerServiceBlockingStub sneakerServiceBlockingStub;


    @Override
    public void reserve(final List<SneakerDTO> sneakers) {
        final List<SneakerRequest> sneakerRequests = sneakers.stream()
                .map(sneakerDTO -> SneakerRequest.newBuilder()
                        .setSneakerId(sneakerDTO.getId())
                        .setSneakerSize(sneakerDTO.getSize())
                        .build())
                .toList();
        final ReserveSneakersRequest reserveSneakersRequest = ReserveSneakersRequest.newBuilder()
                .addAllSneakers(sneakerRequests)
                .build();
        sneakerServiceBlockingStub.reserve(reserveSneakersRequest);
    }

    @Override
    public void cancel(List<SneakerDTO> sneakers) {
        final List<SneakerRequest> sneakerRequests = sneakers.stream()
                .map(sneakerDTO -> SneakerRequest.newBuilder()
                        .setSneakerId(sneakerDTO.getId())
                        .setSneakerSize(sneakerDTO.getSize())
                        .build())
                .toList();
        final ReserveSneakersRequest reserveSneakersRequest = ReserveSneakersRequest.newBuilder()
                .addAllSneakers(sneakerRequests)
                .build();
        sneakerServiceBlockingStub.cancelReservation(reserveSneakersRequest);
    }

    @Override
    public Double getTotalPrice(List<Long> sneakerIds) {
        final SneakerIdsRequest sneakerIdsRequest = SneakerIdsRequest.newBuilder()
                .addAllSneakerId(sneakerIds)
                .build();
        final OrderTotalPriceResponse orderTotalPrice = sneakerServiceBlockingStub.getSneakerPrice(sneakerIdsRequest);
        return orderTotalPrice.getPrice();
    }

    @Override
    public List<ExtendedSneakerDTO> getSneakerDetails(List<Long> sneakerIds) {
        final SneakerIdsRequest sneakerIdsRequest = SneakerIdsRequest.newBuilder()
                .addAllSneakerId(sneakerIds)
                .build();
        final SneakerDetailsResponse sneakerDetailsResponse = sneakerServiceBlockingStub.getSneakerDetails(sneakerIdsRequest);
        return convertToExtendedSneakerDTOList(sneakerDetailsResponse.getSneakerDetailsList());
    }

    @Override
    public List<ExtendedSneakerDetailsDTO> getExtendedSneakerDetails(List<Long> sneakerIds) {
        final SneakerIdsRequest sneakerIdsRequest = SneakerIdsRequest.newBuilder()
                .addAllSneakerId(sneakerIds)
                .build();
        final ExtendedSneakerDetailsResponse sneakerDetailsResponse = sneakerServiceBlockingStub.getExtendedSneakerDetails(sneakerIdsRequest);
        return convertToExtendedSneakerDetailsDTOList(sneakerDetailsResponse.getSneakerDetailsList());
    }
}
