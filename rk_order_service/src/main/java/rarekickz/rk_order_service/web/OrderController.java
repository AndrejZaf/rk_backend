package rarekickz.rk_order_service.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rarekickz.rk_order_service.converter.OrderConverter;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.OrderDTO;
import rarekickz.rk_order_service.dto.OrderIdentifierDTO;
import rarekickz.rk_order_service.dto.OrderInventoryDTO;
import rarekickz.rk_order_service.dto.OrderPreviewDTO;
import rarekickz.rk_order_service.dto.SaleDTO;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;

import static rarekickz.rk_order_service.converter.OrderConverter.toOrderDTO;
import static rarekickz.rk_order_service.converter.OrderConverter.toOrderDTOList;
import static rarekickz.rk_order_service.converter.OrderConverter.toOrderPreviewDTO;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderInventoryService orderInventoryService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<OrderDTO>> getOrders() {
        log.info("Received a request to get all orders");
        final List<Order> orders = orderService.findAll();
        return new ResponseEntity<>(toOrderDTOList(orders), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<SaleDTO>> getOrdersStatistics() {
        log.info("Received a request to get orders statistics");
        final List<SaleDTO> sales = orderService.generateStatistics();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<OrderIdentifierDTO> createOrder(@RequestBody final CreateOrderDTO createOrderDTO) {
        log.info("Received a request to create an order");
        final String paymentSession = orderService.create(createOrderDTO);
        return new ResponseEntity<>(new OrderIdentifierDTO(paymentSession), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<OrderPreviewDTO> fetchOrder(@PathVariable final String id) {
        log.info("Received a request to fetch an order by ID: [{}]", id);
        final Order order = orderService.findByUuid(id);
        final List<OrderInventory> orderInventoryList = orderInventoryService.findAllByOrderId(id);
        final List<OrderInventoryDTO> orderInventoryDTOs = orderInventoryList.stream()
                .map(orderInventory -> new OrderInventoryDTO(orderInventory.getSneakerId(), orderInventory.getSneakerSize()))
                .toList();
        return new ResponseEntity<>(toOrderPreviewDTO(order, orderInventoryDTOs), HttpStatus.OK);
    }
}
