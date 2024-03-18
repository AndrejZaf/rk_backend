package rarekickz.rk_order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rarekickz.rk_order_service.domain.OrderInventory;

import java.util.List;
import java.util.UUID;

public interface OrderInventoryRepository extends JpaRepository<OrderInventory, Long> {

    List<OrderInventory> findByOrderUuid(UUID orderId);
}
