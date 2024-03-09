package rarekickz.rk_order_service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.external.ExternalSneakerService;
import rarekickz.rk_order_service.repository.OrderRepository;
import rarekickz.rk_order_service.service.DeliveryInfoService;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ExternalSneakerService externalSneakerService;
    private final DeliveryInfoService deliveryInfoService;
    private final OrderInventoryService orderInventoryService;
    private final OrderRepository orderRepository;

    @Override
    public Order create(final CreateOrderDTO createOrderDTO) {
        externalSneakerService.reserve(createOrderDTO.getSneakers());
        final DeliveryInfo deliveryInfo = deliveryInfoService.save(createOrderDTO.getDeliveryInfo());
        Order order = createOrder(createOrderDTO);
        order.setDeliveryInfo(deliveryInfo);
        order = orderRepository.save(order);
        orderInventoryService.save(createOrderDTO.getSneakers(), order);
        return order;
    }

    @Override
    public Order findByUuid(final String id) {
        return orderRepository.findByUuid(UUID.fromString(id))
                .orElseThrow(EntityNotFoundException::new);
    }

    private Order createOrder(final CreateOrderDTO createOrderDTO) {
        final List<Long> sneakerIds = createOrderDTO.getSneakers().stream()
                .map(SneakerDTO::getId)
                .toList();
        final Double totalPrice = externalSneakerService.getTotalPrice(sneakerIds);
        return Order.builder()
                .totalPrice(totalPrice)
                .orderStatus(OrderStatus.ORDER_STOCK_RESERVED)
                .build();
    }
}
