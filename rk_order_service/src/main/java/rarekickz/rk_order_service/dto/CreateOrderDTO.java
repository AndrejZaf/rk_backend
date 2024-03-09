package rarekickz.rk_order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {

    private List<SneakerDTO> sneakers;
    private DeliveryInfoDTO deliveryInfo;
}
