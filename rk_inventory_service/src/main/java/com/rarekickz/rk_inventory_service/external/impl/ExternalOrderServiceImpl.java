package com.rarekickz.rk_inventory_service.external.impl;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.OrderServiceGrpc;
import com.rarekickz.proto.lib.PopularSneakerResponse;
import com.rarekickz.rk_inventory_service.external.ExternalOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExternalOrderServiceImpl implements ExternalOrderService {

    @GrpcClient("orderService")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @Override
    public Long findMostPopularSneakerId() {
        log.debug("Requesting the most popular sneaker ID from order service");
        final PopularSneakerResponse mostPopularSneakerResponse = orderServiceBlockingStub.findMostPopularSneaker(Empty.newBuilder().build());
        return mostPopularSneakerResponse.getSneakerId();
    }
}
