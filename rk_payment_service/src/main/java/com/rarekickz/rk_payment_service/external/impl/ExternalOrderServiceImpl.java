package com.rarekickz.rk_payment_service.external.impl;

import com.rarekickz.proto.lib.OrderRequest;
import com.rarekickz.proto.lib.OrderResponse;
import com.rarekickz.proto.lib.OrderServiceGrpc;
import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;
import com.rarekickz.rk_payment_service.external.ExternalOrderService;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import static com.rarekickz.rk_payment_service.external.converter.OrderDetailsConverter.convertToOrderDetailsDTO;

@Slf4j
@Service
public class ExternalOrderServiceImpl implements ExternalOrderService {

    @GrpcClient("orderService")
    private OrderServiceGrpc.OrderServiceBlockingStub orderServiceBlockingStub;

    @Override
    public OrderDetailsDTO getOrderDetails(final String orderId) {
        log.debug("Sending a request to retrieve details for order ID: [{}]", orderId);
        final OrderRequest orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        final OrderResponse orderResponse = orderServiceBlockingStub.getOrderDetails(orderRequest);
        return convertToOrderDetailsDTO(orderResponse);
    }

    @Override
    public void finalizeOrder(final String orderId) {
        log.debug("Sending a request to finalize order with ID: [{}]", orderId);
        final OrderRequest orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        orderServiceBlockingStub.finalizeOrder(orderRequest);
    }

    @Override
    public void cancelOrder(final String orderId) {
        log.debug("Sending a request to cancel order with ID: [{}]", orderId);
        final OrderRequest orderRequest = OrderRequest.newBuilder()
                .setOrderId(orderId)
                .build();
        orderServiceBlockingStub.cancelOrder(orderRequest);
    }
}
