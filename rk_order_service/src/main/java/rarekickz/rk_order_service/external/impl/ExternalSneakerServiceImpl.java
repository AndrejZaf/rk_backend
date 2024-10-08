package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.OrderTotalPriceResponse;
import com.rarekickz.proto.lib.ReserveSneakersRequest;
import com.rarekickz.proto.lib.SneakerDetailsResponse;
import com.rarekickz.proto.lib.SneakerIdsRequest;
import com.rarekickz.proto.lib.SneakerRequest;
import com.rarekickz.proto.lib.SneakerServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.external.ExternalSneakerService;

import java.util.List;

import static rarekickz.rk_order_service.external.converter.SneakerDetailsConverter.convertToExtendedSneakerDTOList;

@Slf4j
@Service
public class ExternalSneakerServiceImpl implements ExternalSneakerService {

    @GrpcClient("sneakerService")
    private SneakerServiceGrpc.SneakerServiceBlockingStub sneakerServiceBlockingStub;

    @Override
    public void reserve(final List<SneakerDTO> sneakers) {
        log.debug("Reserve sneakers");
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
        log.debug("Cancel reservation for sneakers");
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
    public Double getTotalPrice(final List<Long> sneakerIds) {
        log.debug("Retrieve total price for sneakers with IDs: [{}]", sneakerIds);
        final SneakerIdsRequest sneakerIdsRequest = SneakerIdsRequest.newBuilder()
                .addAllSneakerId(sneakerIds)
                .build();
        final OrderTotalPriceResponse orderTotalPrice = sneakerServiceBlockingStub.getSneakerPrice(sneakerIdsRequest);
        return orderTotalPrice.getPrice();
    }

    @Override
    public List<ExtendedSneakerDTO> getSneakerDetails(final List<Long> sneakerIds) {
        log.debug("Retrieve sneaker details for sneakers with IDs: [{}]", sneakerIds);
        final SneakerIdsRequest sneakerIdsRequest = SneakerIdsRequest.newBuilder()
                .addAllSneakerId(sneakerIds)
                .build();
        final SneakerDetailsResponse sneakerDetailsResponse = sneakerServiceBlockingStub.getSneakerDetails(sneakerIdsRequest);
        return convertToExtendedSneakerDTOList(sneakerDetailsResponse.getSneakerDetailsList());
    }
}
