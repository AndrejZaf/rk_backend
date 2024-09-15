package rarekickz.rk_order_service.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {

    @NotEmpty
    private List<SneakerDTO> sneakers;

    @NotNull
    private DeliveryInfoDTO deliveryInfo;
}
