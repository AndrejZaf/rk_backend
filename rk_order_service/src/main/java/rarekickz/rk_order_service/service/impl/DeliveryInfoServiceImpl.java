package rarekickz.rk_order_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;
import rarekickz.rk_order_service.repository.DeliveryInfoRepository;
import rarekickz.rk_order_service.service.DeliveryInfoService;

import static rarekickz.rk_order_service.converter.DeliveryInfoConverter.toDeliveryInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

    private final DeliveryInfoRepository deliveryInfoRepository;

    @Override
    public DeliveryInfo save(final DeliveryInfoDTO deliveryInfoDTO) {
        log.debug("Saving delivery info to the database");
        final DeliveryInfo deliveryInfo = toDeliveryInfo(deliveryInfoDTO);
        return deliveryInfoRepository.save(deliveryInfo);
    }
}
