package rarekickz.rk_order_service.converter;

import lombok.experimental.UtilityClass;
import rarekickz.rk_order_service.domain.OrderInventory;
import rarekickz.rk_order_service.dto.OrderInventoryDTO;

import java.util.List;

@UtilityClass
public class OrderInventoryConverter {

    private static OrderInventoryDTO toOrderInventoryDTO(OrderInventory orderInventory) {
        return new OrderInventoryDTO(orderInventory.getSneakerId(), orderInventory.getSneakerSize());
    }

    public static List<OrderInventoryDTO> toOrderInventoryList(List<OrderInventory> orderInventoryList) {
        return orderInventoryList.stream()
                .map(OrderInventoryConverter::toOrderInventoryDTO)
                .toList();
    }
}
