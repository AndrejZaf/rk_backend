package rarekickz.rk_order_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;
import rarekickz.rk_order_service.dto.ExtendedSneakerDetailsDTO;
import rarekickz.rk_order_service.dto.SaleDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.external.ExternalNotificationService;
import rarekickz.rk_order_service.external.ExternalPaymentService;
import rarekickz.rk_order_service.external.ExternalSneakerService;
import rarekickz.rk_order_service.repository.OrderRepository;
import rarekickz.rk_order_service.service.DeliveryInfoService;
import rarekickz.rk_order_service.service.OrderInventoryService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplUnitTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private ExternalSneakerService externalSneakerService;

    @Mock
    private DeliveryInfoService deliveryInfoService;

    @Mock
    private OrderInventoryService orderInventoryService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ExternalPaymentService externalPaymentService;

    @Mock
    private ExternalNotificationService externalNotificationService;

    private Order order;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .totalPrice(100.0)
                .orderUuid(UUID.randomUUID())
                .orderStatus(OrderStatus.ORDER_STOCK_RESERVED)
                .userId(UUID.randomUUID())
                .build();
    }

    @Test
    void findAll_returnsListOfOrders() {
        // Arrange
        when(orderRepository.findAll()).thenReturn(List.of(order));

        // Act
        List<Order> actualOrders = orderService.findAll();

        // Assert
        assertThat(actualOrders, is(equalTo(List.of(order))));
    }

    @Test
    void create_returnsPaymentUrlForNewlyCreatedOrder() {
        // Arrange
        SneakerDTO sneakerDTO = new SneakerDTO(1L, 15.0);
        DeliveryInfoDTO deliveryInfoDTO = DeliveryInfoDTO.builder().build();
        CreateOrderDTO createOrderDTO = new CreateOrderDTO(List.of(sneakerDTO), deliveryInfoDTO);
        DeliveryInfo deliveryInfo = DeliveryInfo.builder()
                .email("customer@example.com")
                .build();
        String paymentUrl = "http://payment.url";
        SecurityContextHolder.setContext(new SecurityContextImpl(mock(Authentication.class)));
        when(SecurityContextHolder.getContext().getAuthentication().getName()).thenReturn(UUID.randomUUID().toString());
        when(deliveryInfoService.save(deliveryInfoDTO)).thenReturn(deliveryInfo);
        when(orderRepository.save(any())).thenReturn(order);
        when(externalPaymentService.getStripeSessionUrl(order.getOrderUuid().toString())).thenReturn(paymentUrl);

        // Act
        String actualPaymentUrl = orderService.create(createOrderDTO);

        // Assert
        assertThat(actualPaymentUrl, is(equalTo(paymentUrl)));
        verify(externalSneakerService).reserve(createOrderDTO.getSneakers());
        verify(deliveryInfoService).save(deliveryInfoDTO);
        verify(orderInventoryService).save(anyList(), any());
        verify(externalPaymentService).getStripeSessionUrl(order.getOrderUuid().toString());
        verify(externalNotificationService).sendEmailForReservedOrder(deliveryInfo.getEmail(), paymentUrl);
    }

    @Test
    void findByUuid_returnsOrder() {
        // Arrange
        when(orderRepository.findByOrderUuid(order.getOrderUuid())).thenReturn(Optional.of(order));

        // Act
        Order actualOrder = orderService.findByUuid(order.getOrderUuid().toString());

        // Assert
        assertThat(actualOrder, is(equalTo(order)));
    }

    @Test
    void findByUuid_throwsEntityNotFoundException() {
        // Arrange
        when(orderRepository.findByOrderUuid(order.getOrderUuid())).thenReturn(Optional.empty());

        // Act
        // Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.findByUuid(order.getOrderUuid().toString()));
    }

    @Test
    void save_returnsSuccessfullySavedOrder() {
        // Arrange
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Order actualOrder = orderService.save(order);

        // Assert
        assertThat(actualOrder, is(equalTo(order)));
    }

    @Test
    void generateStatistics_returnsListOfSales() {
        // Arrange
        OrderInventory orderInventory = new OrderInventory(1L, 1L, 10.0, order);
        orderInventory.setCreatedDate(LocalDateTime.now());
        List<OrderInventory> orderInventoryList = List.of(orderInventory);
        when(orderInventoryService.findAllInLastWeek()).thenReturn(orderInventoryList);
        ExtendedSneakerDetailsDTO sneakerDetails = new ExtendedSneakerDetailsDTO(1L, "Test", 15.0, "Test");
        List<ExtendedSneakerDetailsDTO> extendedSneakerDetails = List.of(sneakerDetails);
        List<Long> sneakerIds = orderInventoryList.stream()
                .map(OrderInventory::getSneakerId)
                .toList();
        when(externalSneakerService.getExtendedSneakerDetails(sneakerIds)).thenReturn(extendedSneakerDetails);

        // Act
        List<SaleDTO> actualSales = orderService.generateStatistics();

        // Assert
        assertThat(actualSales.size(), is(equalTo(1)));
        verify(orderInventoryService).findAllInLastWeek();
        verify(externalSneakerService).getExtendedSneakerDetails(sneakerIds);
    }
}
