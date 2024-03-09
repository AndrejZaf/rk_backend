package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.external.ExternalSneakerService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class ExternalOrderService extends OrderServiceGrpc.OrderServiceImplBase {

    private final OrderService orderService;
    private final ExternalSneakerService externalSneakerService;

    @Override
    public void getOrderDetails(final OrderRequest request, final StreamObserver<OrderResponse> responseObserver) {
        final String orderUuid = request.getOrderId();
        final Order order = orderService.findByUuid(orderUuid);
        List<Long> sneakerIds = order.getOrderInventory().stream()
                .map(OrderInventory::getId)
                .toList();
        List<ExtendedSneakerDTO> sneakerDetails = externalSneakerService.getSneakerDetails(sneakerIds);
        List<Product> products = sneakerDetails.stream()
                .map(sneaker -> Product.newBuilder()
                        .setId(sneaker.getId())
                        .setName(sneaker.getName())
                        .setPrice(sneaker.getPrice())
                        .build())
                .toList();
        final DeliveryInfo deliveryInfo = order.getDeliveryInfo();
        final OrderResponse orderResponse = OrderResponse.newBuilder()
                .setCustomerDetailsResponse(
                        CustomerDetailsResponse.newBuilder()
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
}
