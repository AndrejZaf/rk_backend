package rarekickz.rk_order_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;
import rarekickz.rk_order_service.repository.DeliveryInfoRepository;
import rarekickz.rk_order_service.service.DeliveryInfoService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryInfoServiceImpl implements DeliveryInfoService {

    private final DeliveryInfoRepository deliveryInfoRepository;

    @Override
    public DeliveryInfo save(final DeliveryInfoDTO deliveryInfoDTO) {
        log.debug("Saving delivery info to the database");
        final DeliveryInfo deliveryInfo = createDeliveryInfoData(deliveryInfoDTO);
        return deliveryInfoRepository.save(deliveryInfo);
    }

    private static DeliveryInfo createDeliveryInfoData(final DeliveryInfoDTO deliveryInfoDTO) {
        return DeliveryInfo.builder()
                .firstName(deliveryInfoDTO.getFirstName())
                .lastName(deliveryInfoDTO.getLastName())
                .email(deliveryInfoDTO.getEmail())
                .phoneNumber(deliveryInfoDTO.getPhoneNumber())
                .city(deliveryInfoDTO.getCity())
                .country(deliveryInfoDTO.getCountry())
                .street(deliveryInfoDTO.getStreet())
                .postalCode(deliveryInfoDTO.getPostalCode())
                .build();
    }
}
