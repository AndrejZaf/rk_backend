package rarekickz.rk_notification_service.service;

import com.rarekickz.proto.lib.EmailNotificationRequest;
import jakarta.mail.MessagingException;

public interface EmailService {

    void sendEmail(EmailNotificationRequest request) throws MessagingException;
}
