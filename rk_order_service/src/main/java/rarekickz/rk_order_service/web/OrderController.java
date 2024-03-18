package rarekickz.rk_order_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.OrderIdentifierDTO;
import rarekickz.rk_order_service.dto.OrderInventoryDTO;
import rarekickz.rk_order_service.dto.OrderVerificationDTO;
import rarekickz.rk_order_service.service.OrderInventoryService;
import rarekickz.rk_order_service.service.OrderService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final OrderInventoryService orderInventoryService;

    @PostMapping
    public ResponseEntity<OrderIdentifierDTO> createOrder(@RequestBody final CreateOrderDTO createOrderDTO) {
        final String paymentSession = orderService.create(createOrderDTO);
        return new ResponseEntity<>(new OrderIdentifierDTO(paymentSession), HttpStatus.OK);
    }

    // TODO: Rename the DTO to something more related to the order
    @GetMapping("/{id}")
    public ResponseEntity<OrderVerificationDTO> fetchOrder(@PathVariable final String id) {
        final Order order = orderService.findByUuid(id);
        final List<OrderInventory> orderInventoryList = orderInventoryService.findAllByOrderId(id);
        final List<OrderInventoryDTO> orderInventoryDTOs = orderInventoryList.stream()
                .map(orderInventory -> new OrderInventoryDTO(orderInventory.getSneakerId(), orderInventory.getSneakerSize()))
                .toList();
        final OrderVerificationDTO orderVerification = OrderVerificationDTO.builder()
                .orderStatus(order.getOrderStatus())
                .uuid(order.getUuid().toString())
                .orderPrice(order.getTotalPrice())
                .orderInventory(orderInventoryDTOs)
                .build();
        return new ResponseEntity<>(orderVerification, HttpStatus.OK);
    }
}
