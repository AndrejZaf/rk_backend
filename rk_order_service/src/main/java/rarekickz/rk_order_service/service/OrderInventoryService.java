package rarekickz.rk_order_service.service;

import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.SneakerDTO;

import java.util.Collection;
import java.util.Set;

public interface OrderInventoryService {

    Set<OrderInventory> save(Collection<SneakerDTO> sneakers, Order order);
}
