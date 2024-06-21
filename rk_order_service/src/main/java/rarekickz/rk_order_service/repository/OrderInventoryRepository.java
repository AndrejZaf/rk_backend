package rarekickz.rk_order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rarekickz.rk_order_service.domain.OrderInventory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderInventoryRepository extends JpaRepository<OrderInventory, Long> {

    List<OrderInventory> findByOrderOrderUuid(UUID orderId);

    @Query("""
         SELECT oi.sneakerId FROM order_inventory oi
         GROUP BY oi.sneakerId ORDER BY COUNT(oi.order) DESC LIMIT 1
         """)
    Long findByMostPopularSneaker();

    List<OrderInventory> findAllByCreatedAtAfter(LocalDateTime date);
}
