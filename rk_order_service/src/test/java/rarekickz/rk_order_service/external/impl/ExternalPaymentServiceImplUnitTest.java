package rarekickz.rk_order_service.external.impl;

import com.rarekickz.proto.lib.PaymentServiceGrpc;
import com.rarekickz.proto.lib.PaymentSessionResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalPaymentServiceImplUnitTest {

    @InjectMocks
    private ExternalPaymentServiceImpl externalPaymentService;

    @Mock
    private PaymentServiceGrpc.PaymentServiceBlockingStub paymentServiceBlockingStub;

    @Test
    void getStripeSessionUrl_successfullyReturnsPaymentUrl() {
        // Arrange
        String orderId = UUID.randomUUID().toString();
        String paymentUrl = "paymentUrl";
        PaymentSessionResponse paymentSessionResponse = PaymentSessionResponse.newBuilder()
                .setSessionUrl(paymentUrl)
                .build();
        when(paymentServiceBlockingStub.createPaymentSession(any())).thenReturn(paymentSessionResponse);

        // Act
        String actualPaymentUrl = externalPaymentService.getStripeSessionUrl(orderId);

        // Assert
        assertThat(actualPaymentUrl, is(equalTo(paymentUrl)));
    }
}
