package rarekickz.rk_order_service.external;

import com.google.protobuf.Empty;
import com.rarekickz.proto.lib.OrderRequest;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.external.impl.ExternalOrderService;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalOrderServiceUnitTest {

    @InjectMocks
    private ExternalOrderService externalOrderService;

    @Mock
    private OrderService orderService;

    @Mock
    private ExternalSneakerService externalSneakerService;

    @Mock
    private OrderInventoryService orderInventoryService;

    @Mock
    private ExternalNotificationService externalNotificationService;

    @Mock
    private StreamObserver responseObserver;

    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .totalPrice(100.0)
                .orderUuid(UUID.randomUUID())
                .orderStatus(OrderStatus.ORDER_STOCK_RESERVED)
                .userId(UUID.randomUUID())
                .build();
        OrderInventory orderInventory = new OrderInventory(1L, 1L, 10.0, order);
        order.setOrderInventory(Set.of(orderInventory));
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .id(1L)
                .firstName("Andrej")
                .lastName("Zafirovski")
                .email("andrei.zafirovski@example.com")
                .phoneNumber("1234567890")
                .city("Skopje")
                .country("Macedonia")
                .street("Somewhere")
                .build();
        order.setDeliveryInfo(deliveryInfo);
        orderRequest = OrderRequest.newBuilder()
                .setOrderId(UUID.randomUUID().toString())
                .build();
    }

    @Test
    void findMostPopularSneaker_sendsTheMostPopularSneakerId() {
        // Arrange
        when(orderInventoryService.findMostPopularSneaker()).thenReturn(1L);

        // Act
        externalOrderService.findMostPopularSneaker(Empty.newBuilder().build(), responseObserver);

        // Assert
        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }

    @Test
    void cancelOrder_cancelsTheOrderAndRestoresTheInventory() {
        // Arrange
        when(orderService.findByOrderId(orderRequest.getOrderId())).thenReturn(order);

        // Act
        externalOrderService.cancelOrder(orderRequest, responseObserver);

        // Assert
        verify(externalSneakerService).cancel(anyList());
        verify(orderService).save(order);
        verify(responseObserver).onNext(Empty.newBuilder().build());
        verify(responseObserver).onCompleted();
    }

    @Test
    void finalizeOrder_successfullyCompletesTheOrder() {
        // Arrange
        when(orderService.findByOrderId(orderRequest.getOrderId())).thenReturn(order);

        // Act
        externalOrderService.finalizeOrder(orderRequest, responseObserver);

        // Assert
        verify(externalNotificationService).sendEmailForSuccessfulOrder(any(), any());
        verify(orderService).save(order);
        verify(responseObserver).onNext(Empty.newBuilder().build());
        verify(responseObserver).onCompleted();
    }

    @Test
    void getOrderDetails_sendOrderDetails() {
        // Arrange
        ExtendedSneakerDTO extendedSneakerDTO = new ExtendedSneakerDTO(1L, "Nike Air Zoom Pegasus 38", 100.0);
        when(orderService.findByOrderId(orderRequest.getOrderId())).thenReturn(order);
        when(externalSneakerService.getSneakerDetails(anyList())).thenReturn(List.of(extendedSneakerDTO));

        // Act
        externalOrderService.getOrderDetails(orderRequest, responseObserver);

        // Assert
        verify(responseObserver).onNext(any());
        verify(responseObserver).onCompleted();
    }
}
