package rarekickz.rk_order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rarekickz.rk_order_service.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUuid(UUID uuid);
}
