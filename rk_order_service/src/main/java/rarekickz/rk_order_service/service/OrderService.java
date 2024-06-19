package rarekickz.rk_order_service.service;

import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.dto.CreateOrderDTO;
import rarekickz.rk_order_service.dto.SaleDTO;

import java.util.List;

public interface OrderService {

    List<Order> findAll();

    String create(CreateOrderDTO createOrderDTO);

    Order findByUuid(String id);

    Order save(Order order);

    List<SaleDTO> generateStatistics();
}
