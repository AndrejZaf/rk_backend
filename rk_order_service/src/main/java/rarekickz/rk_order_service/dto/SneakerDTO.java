package rarekickz.rk_order_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SneakerDTO {

    @NotNull
    private Long id;

    @NotNull
    private Double size;
}
