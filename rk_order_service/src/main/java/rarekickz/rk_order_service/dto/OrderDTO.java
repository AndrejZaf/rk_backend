package rarekickz.rk_order_service.dto;

import lombok.Builder;
import lombok.Data;
import rarekickz.rk_order_service.enums.OrderStatus;

@Data
@Builder
public class OrderDTO {

    private Long id;
    private String email;
    private Double totalPrice;
    private String address;
    private OrderStatus orderStatus;
}
