package rarekickz.rk_order_service.external;

import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;

import java.util.List;

public interface ExternalSneakerService {

    void reserve(List<SneakerDTO> sneakers);

    void cancel(List<SneakerDTO> sneakers);

    Double getTotalPrice(List<Long> sneakerIds);

    List<ExtendedSneakerDTO> getSneakerDetails(List<Long> sneakerIds);
}
