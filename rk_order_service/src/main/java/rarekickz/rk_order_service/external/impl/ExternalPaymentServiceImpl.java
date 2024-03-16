package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.OrderPaymentRequest;
import com.rarekickz.proto.lib.OrderTotalPriceResponse;
import com.rarekickz.proto.lib.PaymentServiceGrpc;
import com.rarekickz.proto.lib.PaymentSessionResponse;
import com.rarekickz.proto.lib.ReserveSneakersRequest;
import com.rarekickz.proto.lib.SneakerDetailsResponse;
import com.rarekickz.proto.lib.SneakerIdsRequest;
import com.rarekickz.proto.lib.SneakerRequest;
import com.rarekickz.proto.lib.SneakerServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import rarekickz.rk_order_service.dto.ExtendedSneakerDTO;
import rarekickz.rk_order_service.dto.SneakerDTO;
import rarekickz.rk_order_service.external.ExternalPaymentService;
import rarekickz.rk_order_service.external.ExternalSneakerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static rarekickz.rk_order_service.external.converter.SneakerDetailsConverter.convertToExtendedSneakerDTOList;

@Service
public class ExternalPaymentServiceImpl implements ExternalPaymentService {

    @GrpcClient("paymentService")
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceBlockingStub;

    @Override
    public String getStripeSessionUrl(final String orderId) {
        PaymentSessionResponse paymentSession = paymentServiceBlockingStub.createPaymentSession(OrderPaymentRequest.newBuilder()
                .setOrderId(orderId)
                .build());
        return paymentSession.getSessionUrl();
    }
}
