package rarekickz.rk_order_service.converter;

import lombok.experimental.UtilityClass;
import rarekickz.rk_order_service.domain.DeliveryInfo;
import rarekickz.rk_order_service.dto.DeliveryInfoDTO;

@UtilityClass
public class DeliveryInfoConverter {

    public static DeliveryInfo toDeliveryInfo(final DeliveryInfoDTO deliveryInfoDTO) {
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

    public static DeliveryInfoDTO toDeliveryInfoDTO(final DeliveryInfo deliveryInfoDTO) {
        return DeliveryInfoDTO.builder()
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
