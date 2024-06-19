package rarekickz.rk_order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class InventorySaleDTO {

    private Long value;
    private String name;
}
