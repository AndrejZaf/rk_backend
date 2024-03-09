package rarekickz.rk_order_service.service;

import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;

public interface DeliveryInfoService {

    DeliveryInfo save(DeliveryInfoDTO deliveryInfoDTO);
}
