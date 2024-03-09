package rarekickz.rk_order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExtendedSneakerDTO {

    private Long id;
    private String name;
    private Double price;
}
