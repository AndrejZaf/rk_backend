package rarekickz.rk_order_service.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.repository.OrderInventoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderInventoryServiceImplUnitTest {

    @InjectMocks
    private OrderInventoryServiceImpl orderInventoryService;

    @Mock
    private OrderInventoryRepository orderInventoryRepository;

    private List<OrderInventory> orderInventoryList;
    private Order order;

    @BeforeEach
    void setup() {
        order = Order.builder()
                .totalPrice(100.0)
                .orderUuid(UUID.randomUUID())
                .orderStatus(OrderStatus.ORDER_STOCK_RESERVED)
                .userId(UUID.randomUUID())
                .build();
        OrderInventory orderInventory = new OrderInventory(1L, 1L, 10.0, order);
        orderInventoryList = List.of(orderInventory);
    }

    @Test
    void findAllInLastWeek_returnsListOfOrderInventories() {
        // Arrange
        when(orderInventoryRepository.findAllByCreatedDateAfter(any())).thenReturn(orderInventoryList);

        // Act
        List<OrderInventory> actualOrderInventory = orderInventoryService.findAllInLastWeek();

        // Assert
        assertThat(actualOrderInventory, is(equalTo(orderInventoryList)));
    }

    @Test
    void findMostPopularSneaker_returnsSneakerId() {
        // Arrange
        when(orderInventoryRepository.findByMostPopularSneaker()).thenReturn(1L);

        // Act
        Long actualMostPopularSneakerId = orderInventoryService.findMostPopularSneaker();

        // Assert
        assertThat(actualMostPopularSneakerId, is(equalTo(1L)));
    }

    @Test
    void findAllByOrderId_returnsListOfOrderInventories() {
        // Arrange
        when(orderInventoryRepository.findByOrderOrderUuid(order.getOrderUuid())).thenReturn(orderInventoryList);

        // Act
        List<OrderInventory> actualOrderInventory = orderInventoryService.findAllByOrderId(order.getOrderUuid().toString());

        // Assert
        assertThat(actualOrderInventory, is(equalTo(orderInventoryList)));
    }

    @Test
    void save_returnsSuccessfullySavedOrderInventory() {
        // Arrange
        SneakerDTO sneakerDTO = new SneakerDTO(1L, 15.0);
        when(orderInventoryRepository.saveAll(anyList())).thenReturn(orderInventoryList);

        // Act
        Set<OrderInventory> actualOrderInventoryList = orderInventoryService.save(List.of(sneakerDTO), order);

        // Assert
        assertThat(actualOrderInventoryList, is(equalTo(new HashSet<>(orderInventoryList))));
    }
}
