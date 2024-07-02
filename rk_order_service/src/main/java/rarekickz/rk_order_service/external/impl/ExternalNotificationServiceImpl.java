package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.EmailNotificationRequest;
import com.rarekickz.proto.lib.EmailOrderType;
import com.rarekickz.proto.lib.NotificationServiceGrpc;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.external.ExternalNotificationService;

@Slf4j
@Service
public class ExternalNotificationServiceImpl implements ExternalNotificationService {

    @GrpcClient("notificationService")
    private NotificationServiceGrpc.NotificationServiceFutureStub notificationServiceBlockingStub;

    @Override
    public void sendEmailForReservedOrder(final String to, final String paymentUrl) {
        log.debug("Sending a request to send an email for reserved order to: [{}]", to);
        final EmailNotificationRequest request = EmailNotificationRequest.newBuilder()
                .setPaymentUrl(paymentUrl)
                .setEmailOrderType(EmailOrderType.RESERVED)
                .setReceiver(to)
                .build();
        notificationServiceBlockingStub.sendEmailForOrder(request);
    }

    @Override
    public void sendEmailForSuccessfulOrder(final String to, final String orderId) {
        log.debug("Sending a request to send an email for successful order to: [{}]", to);
        final EmailNotificationRequest request = EmailNotificationRequest.newBuilder()
                .setOrderId(orderId)
                .setEmailOrderType(EmailOrderType.SUCCESSFUL)
                .setReceiver(to)
                .build();
        notificationServiceBlockingStub.sendEmailForOrder(request);
    }
}
