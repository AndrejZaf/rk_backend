package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.NotificationServiceGrpc;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExternalNotificationServiceImplUnitTest {

    @InjectMocks
    private ExternalNotificationServiceImpl externalNotificationService;

    @Mock
    private NotificationServiceGrpc.NotificationServiceFutureStub notificationServiceBlockingStub;


    @Test
    void sendEmailForReservedOrder_successfullySendsEmail() {
        // Arrange
        final String to = "test@email.com";
        final String paymentUrl = "http://payment.com";

        // Act
        externalNotificationService.sendEmailForReservedOrder(to, paymentUrl);

        // Assert
        verify(notificationServiceBlockingStub).sendEmailForOrder(any());
    }

    @Test
    void sendEmailForSuccessfulOrder_successfullySendsEmail() {
        // Arrange
        final String to = "test@email.com";
        final String orderId = UUID.randomUUID().toString();

        // Act
        externalNotificationService.sendEmailForSuccessfulOrder(to, orderId);

        // Assert
        verify(notificationServiceBlockingStub).sendEmailForOrder(any());
    }
}
