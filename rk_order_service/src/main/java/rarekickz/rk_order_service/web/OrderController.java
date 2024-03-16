package rarekickz.rk_order_service.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.OrderIdentifierDTO;
import rarekickz.rk_order_service.dto.OrderVerificationDTO;
import rarekickz.rk_order_service.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderIdentifierDTO> createOrder(@RequestBody final CreateOrderDTO createOrderDTO) {
        final String paymentSession = orderService.create(createOrderDTO);
        return new ResponseEntity<>(new OrderIdentifierDTO(paymentSession), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderVerificationDTO> verifyOrder(@PathVariable final String id) {
        final Order order = orderService.findByUuid(id);
        final OrderVerificationDTO orderVerification = OrderVerificationDTO.builder()
                .orderStatus(order.getOrderStatus())
                .uuid(order.getUuid().toString())
                .orderPrice(order.getTotalPrice())
                .build();
        return new ResponseEntity<>(orderVerification, HttpStatus.OK);
    }
}
