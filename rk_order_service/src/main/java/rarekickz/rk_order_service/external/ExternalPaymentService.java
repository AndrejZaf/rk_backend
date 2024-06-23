package rarekickz.rk_order_service.external;

public interface ExternalPaymentService {

    String getStripeSessionUrl(String orderId);
}
