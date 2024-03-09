package rarekickz.rk_order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import rarekickz.rk_order_service.enums.OrderStatus;

@Data
@Builder
public class OrderVerificationDTO {

    private OrderStatus orderStatus;
    private String uuid;
    private Double orderPrice;
}
