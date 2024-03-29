package com.rarekickz.rk_payment_service.external.impl;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.OrderRequest;
import com.rarekickz.proto.lib.OrderResponse;
import com.rarekickz.proto.lib.OrderServiceGrpc;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import io.grpc.ManagedChannel;
import net.devh.boot.grpc.client.inject.GrpcClient;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Service;

import static com.rarekickz.rk_payment_service.external.converter.OrderDetailsConverter.convertToOrderDetailsDTO;

@Service
public class ExternalOrderServiceImpl implements ExternalOrderService {

    @GrpcClient("orderService")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @Override
    public OrderDetailsDTO getOrderDetails(String orderId) {
        final OrderRequest orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        final OrderResponse orderResponse = orderServiceBlockingStub.getOrderDetails(orderRequest);
        return convertToOrderDetailsDTO(orderResponse);
    }

    @Override
    public void finalizeOrder(String orderId) {
        final OrderRequest orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        orderServiceBlockingStub.finalizeOrder(orderRequest);
    }

    @Override
    public void cancelOrder(String orderId) {
        final OrderRequest orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        orderServiceBlockingStub.cancelOrder(orderRequest);
    }
}
