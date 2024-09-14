package rarekickz.rk_notification_service.external.impl;

import com.rarekickz.proto.lib.EmailNotificationRequest;
import com.rarekickz.proto.lib.EmailOrderType;
import io.grpc.stub.StreamObserver;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rarekickz.rk_notification_service.service.EmailService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExternalNotificationServiceImplUnitTest {

    @InjectMocks
    private ExternalNotificationServiceImpl externalNotificationService;

    @Mock
    private EmailService emailService;

    @Mock
    private StreamObserver responseObserver;

    @Test
    void sendEmailForOrder_successfullySendsAnEmail() throws MessagingException {
        // Arrange
        EmailNotificationRequest request = EmailNotificationRequest.newBuilder()
                .setOrderId(UUID.randomUUID().toString())
                .setReceiver("test@gmail.com")
                .setEmailOrderType(EmailOrderType.SUCCESSFUL)
                .setPaymentUrl("test")
                .build();

        // Act
        externalNotificationService.sendEmailForOrder(request, responseObserver);

        // Assert
        verify(emailService).sendEmail(request);
    }

    @Test
    void sendEmailForOrder_throwsMessagingException() throws MessagingException {
        // Arrange
        EmailNotificationRequest request = EmailNotificationRequest.newBuilder()
                .setOrderId(UUID.randomUUID().toString())
                .setReceiver("test@gmail.com")
                .setEmailOrderType(EmailOrderType.SUCCESSFUL)
                .setPaymentUrl("test")
                .build();
        doThrow(MessagingException.class).when(emailService).sendEmail(request);

        // Act
        externalNotificationService.sendEmailForOrder(request, responseObserver);

        // Assert
        verify(responseObserver).onError(any());
    }
}
