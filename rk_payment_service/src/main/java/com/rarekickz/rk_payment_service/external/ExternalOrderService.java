package com.rarekickz.rk_payment_service.external;

import com.rarekickz.rk_payment_service.dto.OrderDetailsDTO;

public interface ExternalOrderService {

    OrderDetailsDTO getOrderDetails(String orderId);
}
