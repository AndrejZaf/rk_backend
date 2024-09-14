package rarekickz.rk_notification_service.service.impl;

import com.rarekickz.proto.lib.EmailNotificationRequest;
import com.rarekickz.proto.lib.EmailOrderType;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.ITemplateEngine;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplUnitTest {

    @InjectMocks
    private EmailServiceImpl emailService;

    @Mock
    private JavaMailSenderImpl javaMailSender;

    @Mock
    private ITemplateEngine templateEngine;


    @ParameterizedTest
    @EnumSource(value = EmailOrderType.class, names = {"SUCCESSFUL", "RESERVED"})
    void sendEmail_successfullySendEmailForPaidReservation(EmailOrderType emailOrderType) throws MessagingException {
        // Arrange
        MimeMessage mimeMessage = mock(MimeMessage.class);
        EmailNotificationRequest request = EmailNotificationRequest.newBuilder()
                .setOrderId(UUID.randomUUID().toString())
                .setReceiver("test@gmail.com")
                .setEmailOrderType(emailOrderType)
                .setPaymentUrl("test")
                .build();
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process((String) any(), any())).thenReturn("test-content");

        // Act
        emailService.sendEmail(request);

        // Assert
        verify(javaMailSender).send(mimeMessage);
    }

//    @Test
//    void sendEmail_successfullySendEmailForSuccessfulReservation() throws MessagingException {
//        // Arrange
//        MimeMessage mimeMessage = mock(MimeMessage.class);
//        EmailNotificationRequest request = EmailNotificationRequest.newBuilder()
//                .setOrderId(UUID.randomUUID().toString())
//                .setReceiver("test@gmail.com")
//                .setEmailOrderType(EmailOrderType.RESERVED)
//                .setPaymentUrl("test")
//                .build();
//        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
//        when(templateEngine.process((String) any(), any())).thenReturn("test-content");
//
//        // Act
//        emailService.sendEmail(request);
//
//        // Assert
//        verify(javaMailSender).send(mimeMessage);
//    }
}
