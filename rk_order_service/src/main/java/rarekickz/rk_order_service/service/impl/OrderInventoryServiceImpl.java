package rarekickz.rk_order_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.domain.Order;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.repository.OrderInventoryRepository;
import rarekickz.rk_order_service.service.OrderInventoryService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderInventoryServiceImpl implements OrderInventoryService {

    private final OrderInventoryRepository orderInventoryRepository;

    @Override
    public Set<OrderInventory> save(final Collection<SneakerDTO> sneakers, Order order) {
        final List<OrderInventory> orderInventory = sneakers.stream()
                .map(sneaker -> OrderInventory.builder()
                        .sneakerId(sneaker.getId())
                        .sneakerSize(sneaker.getSize())
                        .order(order)
                        .build())
                .toList();
        return new HashSet<>(orderInventoryRepository.saveAll(orderInventory));
    }

    @Override
    public List<OrderInventory> findAllByOrderId(String orderId) {
        return orderInventoryRepository.findByOrderUuid(UUID.fromString(orderId));
    }

    @Override
    public Long findMostPopularSneaker() {
        return orderInventoryRepository.findByMostPopularSneaker();
    }

    @Override
    public List<OrderInventory> findAllInLastWeek() {
        return orderInventoryRepository.findAllByCreatedAtAfter(LocalDateTime.now().minusWeeks(1));
    }
}
