package rarekickz.rk_order_service.external;

import java.util.List;

public interface ExternalPaymentService {

    String getStripeSessionUrl(String orderId);
}
