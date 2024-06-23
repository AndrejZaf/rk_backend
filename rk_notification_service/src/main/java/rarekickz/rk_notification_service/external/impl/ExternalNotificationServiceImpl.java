package rarekickz.rk_notification_service.external.impl;

import com.google.protobuf.Empty;
import com.google.rpc.Code;
import com.google.rpc.Status;
import com.rarekickz.proto.lib.EmailNotificationRequest;
import com.rarekickz.proto.lib.NotificationServiceGrpc;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import rarekickz.rk_notification_service.service.EmailService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class ExternalNotificationServiceImpl extends NotificationServiceGrpc.NotificationServiceImplBase {

    private final EmailService emailService;

    @Override
    public void sendEmailForOrder(final EmailNotificationRequest request, final StreamObserver<Empty> responseObserver) {
        log.info("Received a request to send email to: [{}]", request.getReceiver());
        try {
            emailService.sendEmail(request);
        } catch (MessagingException e) {
            final Status status = Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT_VALUE)
                    .setMessage("Currently unable to send email")
                    .build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(status));
        }
    }
}
