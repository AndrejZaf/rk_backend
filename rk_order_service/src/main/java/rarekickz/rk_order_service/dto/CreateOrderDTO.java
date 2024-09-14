package rarekickz.rk_order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateOrderDTO {

    private List<SneakerDTO> sneakers;
    private DeliveryInfoDTO deliveryInfo;
}
