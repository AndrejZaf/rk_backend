package rarekickz.rk_order_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryInfoDTO {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String city;
    private String country;
    private String street;
    private String postalCode;
}
