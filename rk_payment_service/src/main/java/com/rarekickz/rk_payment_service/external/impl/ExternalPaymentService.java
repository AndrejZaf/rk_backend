package com.rarekickz.rk_payment_service.external.impl;

import com.rarekickz.proto.lib.OrderPaymentRequest;
import com.rarekickz.proto.lib.PaymentServiceGrpc;
import com.rarekickz.proto.lib.PaymentSessionResponse;
import com.rarekickz.rk_payment_service.service.StripeService;
import com.stripe.model.checkout.Session;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ExternalPaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final StripeService stripeService;

    @Override
    @SneakyThrows
    public void createPaymentSession(final OrderPaymentRequest request, final StreamObserver<PaymentSessionResponse> responseObserver) {
        log.debug("Received request to create payment session for order ID: [{}]", request.getOrderId());
        final Session session = stripeService.generateSession(request.getOrderId());
        responseObserver.onNext(PaymentSessionResponse.newBuilder().setSessionUrl(session.getUrl()).build());
        responseObserver.onCompleted();
    }
}
