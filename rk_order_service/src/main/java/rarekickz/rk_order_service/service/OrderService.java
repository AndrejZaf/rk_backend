package rarekickz.rk_order_service.service;

import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.dto.CreateOrderDTO;

public interface OrderService {

    String create(CreateOrderDTO createOrderDTO);

    Order findByUuid(String id);
}
