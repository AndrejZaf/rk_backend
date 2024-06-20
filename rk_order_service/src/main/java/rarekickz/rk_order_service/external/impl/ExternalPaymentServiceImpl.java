package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.OrderPaymentRequest;
import com.rarekickz.proto.lib.PaymentServiceGrpc;
import com.rarekickz.proto.lib.PaymentSessionResponse;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.external.ExternalPaymentService;

@Slf4j
@Service
public class ExternalPaymentServiceImpl implements ExternalPaymentService {

    @GrpcClient("paymentService")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceBlockingStub;

    @Override
    public String getStripeSessionUrl(final String orderId) {
        log.debug("Retrieve Stripe session URL for order with ID: [{}]", orderId);
        final PaymentSessionResponse paymentSession = paymentServiceBlockingStub.createPaymentSession(OrderPaymentRequest.newBuilder()
                .setOrderId(orderId)
                .build());
        return paymentSession.getSessionUrl();
    }
}
