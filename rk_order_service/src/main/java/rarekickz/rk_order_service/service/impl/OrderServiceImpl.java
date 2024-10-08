package rarekickz.rk_order_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.BrandDTO;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.InventorySaleDTO;
import rarekickz.rk_order_service.dto.SaleDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.enums.OrderStatus;
import rarekickz.rk_order_service.exception.OrderNotFoundException;
import rarekickz.rk_order_service.external.ExternalBrandService;
import rarekickz.rk_order_service.external.ExternalNotificationService;
import rarekickz.rk_order_service.external.ExternalPaymentService;
import rarekickz.rk_order_service.external.ExternalSneakerService;
import rarekickz.rk_order_service.repository.OrderRepository;
import rarekickz.rk_order_service.service.DeliveryInfoService;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ExternalSneakerService externalSneakerService;
    private final DeliveryInfoService deliveryInfoService;
    private final OrderInventoryService orderInventoryService;
    private final OrderRepository orderRepository;
    private final ExternalPaymentService externalPaymentService;
    private final ExternalNotificationService externalNotificationService;
    private final ExternalBrandService externalBrandService;

    @Override
    public List<Order> findAll() {
        log.debug("Retrieving all orders from the database");
        return orderRepository.findAll();
    }

    @Override
    public String create(final CreateOrderDTO createOrderDTO) {
        log.debug("Creating new order");
        externalSneakerService.reserve(createOrderDTO.getSneakers());
        final DeliveryInfo deliveryInfo = deliveryInfoService.save(createOrderDTO.getDeliveryInfo());
        Order order = createOrder(createOrderDTO);
        order.setUserId(UUID.fromString(SecurityContextHolder.getContext().getAuthentication().getName()));
        order.setDeliveryInfo(deliveryInfo);
        order = orderRepository.save(order);
        orderInventoryService.save(createOrderDTO.getSneakers(), order);
        final String paymentUrl = externalPaymentService.getStripeSessionUrl(order.getOrderUuid().toString());
        externalNotificationService.sendEmailForReservedOrder(deliveryInfo.getEmail(), paymentUrl);
        return paymentUrl;
    }

    @Override
    public Order findByOrderId(final String orderId) {
        log.debug("Retrieving order from the database by ID: [{}]", orderId);
        return orderRepository.findByOrderUuid(UUID.fromString(orderId))
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Order save(final Order order) {
        log.debug("Saving order to the database");
        return orderRepository.save(order);
    }

    @Override
    public List<SaleDTO> generateStatistics() {
        log.debug("Generating statistics");
        final Map<Long, BrandDTO> brandIdToBrandMap = externalBrandService.getAllBrands();
        final List<OrderInventory> orderInventoryList = orderInventoryService.findAllInLastWeek();
        final Map<String, Map<LocalDate, List<OrderInventory>>> brandToSalesPerDate = orderInventoryList.stream()
                .collect(Collectors.groupingBy(orderInventory -> brandIdToBrandMap.get(orderInventory.getBrandId()).getName(),
                        Collectors.groupingBy(orderInventory -> orderInventory.getCreatedDate().toLocalDate())));
        final List<SaleDTO> totalSales = new ArrayList<>();
        brandToSalesPerDate.forEach((brandName, salesPerDate) -> {
            final SaleDTO sales = new SaleDTO(brandName, new ArrayList<>());
            salesPerDate.forEach((localDate, orderInventories) -> {
                final InventorySaleDTO inventorySaleDTO = new InventorySaleDTO((long) orderInventories.size(), localDate.toString());
                sales.getSeries().add(inventorySaleDTO);
            });
            totalSales.add(sales);
        });
        return totalSales;
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
