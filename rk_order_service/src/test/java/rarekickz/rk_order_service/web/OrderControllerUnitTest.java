package rarekickz.rk_order_service.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;
import rarekickz.rk_order_service.dto.InventorySaleDTO;
import rarekickz.rk_order_service.dto.OrderDTO;
import rarekickz.rk_order_service.dto.OrderIdentifierDTO;
import rarekickz.rk_order_service.dto.OrderPreviewDTO;
import rarekickz.rk_order_service.dto.SaleDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static rarekickz.rk_order_service.converter.DeliveryInfoConverter.toDeliveryInfoDTO;
import static rarekickz.rk_order_service.converter.OrderConverter.toOrderDTOList;

@ExtendWith(MockitoExtension.class)
class OrderControllerUnitTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Mock
    private OrderInventoryService orderInventoryService;

    private Order order;
    private DeliveryInfo deliveryInfo;

    private String postalCode;

    @BeforeEach
    void setUp() {
        deliveryInfo = DeliveryInfo.builder()
                .id(1L)
                .firstName("Andrej")
                .lastName("Zafirovski")
                .email("andrei.zafirovski@example.com")
                .phoneNumber("1234567890")
                .city("Skopje")
                .country("Macedonia")
                .street("Somewhere")
                .build();
        order = Order.builder()
                .totalPrice(100.0)
                .orderUuid(UUID.randomUUID())
                .deliveryInfo(deliveryInfo)
                .orderStatus(OrderStatus.ORDER_STOCK_RESERVED)
                .userId(UUID.randomUUID())
                .build();
        deliveryInfo.setOrder(order);
    }

    @Test
    void getOrders_returnsListOfOrders() {
        // Arrange
        when(orderService.findAll()).thenReturn(List.of(order));
        List<OrderDTO> orders = toOrderDTOList(List.of(order));

        // Act
        ResponseEntity<List<OrderDTO>> actualOrders = orderController.getOrders();

        // Assert
        assertThat(actualOrders.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(actualOrders.getBody(), is(equalTo(orders)));
    }

    @Test
    void getOrdersStatistics_returnsSaleStats() {
        // Arrange
        SaleDTO saleDTO = new SaleDTO("Jordan", List.of(new InventorySaleDTO(10L, "Jordan")));
        List<SaleDTO> sales = List.of(saleDTO);
        when(orderService.generateStatistics()).thenReturn(sales);

        // Act
        ResponseEntity<List<SaleDTO>> actualSales = orderController.getOrdersStatistics();

        // Assert
        assertThat(actualSales.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(actualSales.getBody(), is(equalTo(sales)));
    }

    @Test
    void createOrder_successfullyCreatesOrder() {
        // Arrange
        String paymentUrl = "paymentUrl";
        SneakerDTO sneakerDTO = new SneakerDTO(1L, 15.0);
        DeliveryInfoDTO deliveryInfoDTO = toDeliveryInfoDTO(deliveryInfo);
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(List.of(sneakerDTO), deliveryInfoDTO);
        when(orderService.create(createOrderDTO)).thenReturn(paymentUrl);

        // Act
        ResponseEntity<OrderIdentifierDTO> actualOrderIdentifier = orderController.createOrder(createOrderDTO);

        // Assert
        assertThat(actualOrderIdentifier.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
        assertThat(actualOrderIdentifier.getBody().getSessionUrl(), is(equalTo(paymentUrl)));
    }

    @Test
    void fetchOrder_returnsOrderPreviewList() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        OrderInventory orderInventory = new OrderInventory(1L, 1L, 1L, 10.0, order);
        when(orderService.findByOrderId(orderId.toString())).thenReturn(order);
        when(orderInventoryService.findAllByOrderId(orderId.toString())).thenReturn(List.of(orderInventory));

        // Act
        ResponseEntity<OrderPreviewDTO> actualOrderPreview = orderController.fetchOrder(orderId.toString());

        // Assert
        assertThat(actualOrderPreview.getStatusCode(), is(equalTo(HttpStatus.OK)));
        assertThat(actualOrderPreview.getBody().getOrderStatus(), is(equalTo(order.getOrderStatus())));
    }
}
