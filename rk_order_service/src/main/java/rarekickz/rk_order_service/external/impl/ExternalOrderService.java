package rarekickz.rk_order_service.external.impl;

import com.google.protobuf.Empty;
import com.google.protobuf.Int64Value;
import com.rarekickz.proto.lib.CustomerDetailsResponse;
import com.rarekickz.proto.lib.OrderRequest;
import com.rarekickz.proto.lib.OrderResponse;
import com.rarekickz.proto.lib.OrderServiceGrpc;
import com.rarekickz.proto.lib.PopularSneakerResponse;
import com.rarekickz.proto.lib.Product;
import com.rarekickz.proto.lib.SelectedProductsResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.external.ExternalNotificationService;
import rarekickz.rk_order_service.external.ExternalSneakerService;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;

import static java.util.Objects.nonNull;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ExternalOrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderService orderService;
    private final ExternalSneakerService externalSneakerService;
    private final OrderInventoryService orderInventoryService;
    private final ExternalNotificationService externalNotificationService;

    @Override
    public void getOrderDetails(final OrderRequest request, final StreamObserver<OrderResponse> responseObserver) {
        log.debug("Received a request to get the order details from the database");
        final String orderUuid = request.getOrderId();
        final Order order = orderService.findByOrderId(orderUuid);
        final List<Long> sneakerIds = order.getOrderInventory().stream()
                .map(OrderInventory::getSneakerId)
                .toList();
        final List<ExtendedSneakerDTO> sneakerDetails = externalSneakerService.getSneakerDetails(sneakerIds);
        final List<Product> products = sneakerDetails.stream()
                .map(sneaker -> Product.newBuilder()
                        .setId(sneaker.getId())
                        .setName(sneaker.getName())
                        .setPrice(sneaker.getPrice())
                        .build())
                .toList();
        final DeliveryInfo deliveryInfo = order.getDeliveryInfo();
        final OrderResponse orderResponse = OrderResponse.newBuilder()
                .setCustomerDetailsResponse(CustomerDetailsResponse.newBuilder()
                        .setEmail(deliveryInfo.getEmail())
                        .setName(String.format("%s %s", deliveryInfo.getFirstName(), deliveryInfo.getLastName()))
                        .build())
                .setSelectedProductResponse(SelectedProductsResponse.newBuilder()
                        .addAllProducts(products)
                        .build())
                .build();
        responseObserver.onNext(orderResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void finalizeOrder(final OrderRequest request, final StreamObserver<Empty> responseObserver) {
        log.debug("Received a request to finalize order with ID: [{}]", request.getOrderId());
        final Order order = orderService.findByOrderId(request.getOrderId());
        order.setOrderStatus(OrderStatus.ORDER_PAID);
        orderService.save(order);
        externalNotificationService.sendEmailForSuccessfulOrder(order.getDeliveryInfo().getEmail(), order.getOrderUuid().toString());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void cancelOrder(final OrderRequest request, final StreamObserver<Empty> responseObserver) {
        log.debug("Received a request to cancel order with ID: [{}]", request.getOrderId());
        final Order order = orderService.findByOrderId(request.getOrderId());
        order.setOrderStatus(OrderStatus.ORDER_CANCELLED);
        final List<SneakerDTO> sneakers = order.getOrderInventory().stream()
                .map(orderInv -> new SneakerDTO(orderInv.getSneakerId(), orderInv.getSneakerSize()))
                .toList();
        externalSneakerService.cancel(sneakers);
        orderService.save(order);
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void findMostPopularSneaker(final Empty request, final StreamObserver<PopularSneakerResponse> responseObserver) {
        log.debug("Received a request to find the most popular sneaker");
        final Long mostPopularSneakerId = orderInventoryService.findMostPopularSneaker();
        final PopularSneakerResponse.Builder responseBuilder = PopularSneakerResponse.newBuilder();
        if (nonNull(mostPopularSneakerId)) {
            responseBuilder.setSneakerId(Int64Value.of(mostPopularSneakerId));
        }

        final PopularSneakerResponse popularSneakerResponse = responseBuilder.build();
        responseObserver.onNext(popularSneakerResponse);
        responseObserver.onCompleted();
    }
}
