package rarekickz.rk_notification_service.service.impl;

import com.rarekickz.proto.lib.EmailNotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import rarekickz.rk_notification_service.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String ORDER_SUCCESSFUL_TEMPLATE = "order-successful";
    private static final String ORDER_RESERVED_TEMPLATE = "order-reserved";

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Value("${client.base-url}")
    private String clientBaseUrl;

    @Override
    public void sendEmail(EmailNotificationRequest request) throws MessagingException {
        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        email.setTo(request.getReceiver());
        email.setFrom("noreply@rarekickz.com");
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        final String htmlContent = getTemplate(request, ctx, email);
        email.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
    }

    private String getTemplate(EmailNotificationRequest request, Context context, MimeMessageHelper email) throws MessagingException {
        switch (request.getEmailOrderType()) {
            case RESERVED -> {
                email.setSubject("Your new kicks are on the way!");
                context.setVariable("url", String.format("%s/orders/%s", clientBaseUrl, request.getOrderId()));
                return this.templateEngine.process(ORDER_SUCCESSFUL_TEMPLATE, context);
            }
            case SUCCESSFUL -> {
                email.setSubject("Your kicks are waiting in order to be shipped!");
                context.setVariable("url", request.getPaymentUrl());
                return this.templateEngine.process(ORDER_RESERVED_TEMPLATE, context);
            }
        }
        return StringUtils.EMPTY;
    }
}
