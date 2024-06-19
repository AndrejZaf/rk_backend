package rarekickz.rk_order_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderInventoryService orderInventoryService;

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getOrders() {
        final List<OrderDTO> orders = orderService.findAll().stream()
                .map(order -> OrderDTO.builder()
                        .id(order.getId())
                        .totalPrice(order.getTotalPrice())
                        .orderStatus(order.getOrderStatus())
                        .email(order.getDeliveryInfo().getEmail())
                        .address(order.getDeliveryInfo().getStreet())
                        .build())
                .toList();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<SaleDTO>> getOrdersStatistics() {
        List<SaleDTO> sales = orderService.generateStatistics();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrderIdentifierDTO> createOrder(@RequestBody final CreateOrderDTO createOrderDTO) {
        final String paymentSession = orderService.create(createOrderDTO);
        return new ResponseEntity<>(new OrderIdentifierDTO(paymentSession), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderPreviewDTO> fetchOrder(@PathVariable final String id) {
        final Order order = orderService.findByUuid(id);
        final List<OrderInventory> orderInventoryList = orderInventoryService.findAllByOrderId(id);
        final List<OrderInventoryDTO> orderInventoryDTOs = orderInventoryList.stream()
                .map(orderInventory -> new OrderInventoryDTO(orderInventory.getSneakerId(), orderInventory.getSneakerSize()))
                .toList();
        final OrderPreviewDTO orderPreviewDTO = OrderPreviewDTO.builder()
                .orderStatus(order.getOrderStatus())
                .uuid(order.getUuid().toString())
                .orderPrice(order.getTotalPrice())
                .orderInventory(orderInventoryDTOs)
                .build();
        return new ResponseEntity<>(orderPreviewDTO, HttpStatus.OK);
    }
}
