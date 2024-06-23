package rarekickz.rk_order_service.external;

public interface ExternalNotificationService {

    void sendEmailForReservedOrder(String to, String paymentUrl);

    void sendEmailForSuccessfulOrder(String to, String orderId);
}
