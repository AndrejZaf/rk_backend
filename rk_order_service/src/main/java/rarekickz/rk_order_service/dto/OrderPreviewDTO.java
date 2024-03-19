package rarekickz.rk_order_service.dto;

import lombok.Builder;
import lombok.Data;
import rarekickz.rk_order_service.enums.OrderStatus;

import java.util.List;

@Data
@Builder
public class OrderPreviewDTO {

    private OrderStatus orderStatus;
    private String uuid;
    private Double orderPrice;
    private List<OrderInventoryDTO> orderInventory;
}
