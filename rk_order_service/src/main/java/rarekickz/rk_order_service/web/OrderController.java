package rarekickz.rk_order_service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

import static rarekickz.rk_order_service.converter.OrderConverter.toOrderDTOList;
import static rarekickz.rk_order_service.converter.OrderConverter.toOrderPreviewDTO;
import static rarekickz.rk_order_service.converter.OrderInventoryConverter.toOrderInventoryList;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final OrderInventoryService orderInventoryService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Retrieve all orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all orders"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<OrderDTO>> getOrders() {
        log.info("Received a request to get all orders");
        final List<Order> orders = orderService.findAll();
        return new ResponseEntity<>(toOrderDTOList(orders), HttpStatus.OK);
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Generate sneaker sale statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales statistics"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<SaleDTO>> getOrdersStatistics() {
        log.info("Received a request to get orders statistics");
        final List<SaleDTO> sales = orderService.generateStatistics();
        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Create order for a sneakers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sales statistics"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderIdentifierDTO> createOrder(@Valid @RequestBody final CreateOrderDTO createOrderDTO) {
        log.info("Received a request to create an order");
        final String paymentSession = orderService.create(createOrderDTO);
        return new ResponseEntity<>(new OrderIdentifierDTO(paymentSession), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Fetch order by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched order"),
            @ApiResponse(responseCode = "404", description = "Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<OrderPreviewDTO> fetchOrder(@PathVariable final String id) {
        log.info("Received a request to fetch an order by ID: [{}]", id);
        final Order order = orderService.findByOrderId(id);
        final List<OrderInventory> orderInventoryList = orderInventoryService.findAllByOrderId(id);
        final List<OrderInventoryDTO> orderInventoryDTOs = toOrderInventoryList(orderInventoryList);
        return new ResponseEntity<>(toOrderPreviewDTO(order, orderInventoryDTOs), HttpStatus.OK);
    }
}
