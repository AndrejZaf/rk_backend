package rarekickz.rk_notification_service.service.impl;

import com.rarekickz.proto.lib.EmailNotificationRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import rarekickz.rk_notification_service.service.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final String ORDER_SUCCESSFUL_TEMPLATE = "order-successful";
    private static final String ORDER_RESERVED_TEMPLATE = "order-reserved";

    private final JavaMailSenderImpl javaMailSender;
    private final ITemplateEngine templateEngine;

    @Value("${client.base-url}")
    private String clientBaseUrl;

    @Override
    public void sendEmail(final EmailNotificationRequest request) throws MessagingException {
        log.debug("Sending email to [{}]", request.getReceiver());
        final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        final MimeMessageHelper email = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        email.setTo(request.getReceiver());
        email.setFrom("noreply@rarekickz.com");
        final Context ctx = new Context(LocaleContextHolder.getLocale());
        final String htmlContent = getTemplate(request, ctx, email);
        email.setText(htmlContent, true);
        javaMailSender.send(mimeMessage);
        log.debug("Email sent successfully to [{}]", request.getReceiver());
    }

    private String getTemplate(final EmailNotificationRequest request, final Context context,
                               final MimeMessageHelper email) throws MessagingException {
        switch (request.getEmailOrderType()) {
            case SUCCESSFUL -> {
                email.setSubject("Your new kicks are on the way!");
                context.setVariable("url", String.format("%s/orders/%s", clientBaseUrl, request.getOrderId()));
                return templateEngine.process(ORDER_SUCCESSFUL_TEMPLATE, context);
            }
            case RESERVED -> {
                email.setSubject("Your kicks are waiting in order to be shipped!");
                context.setVariable("url", request.getPaymentUrl());
                return templateEngine.process(ORDER_RESERVED_TEMPLATE, context);
            }
            default -> log.warn("Unknown order type: [{}]", request.getEmailOrderType());
        }
        return StringUtils.EMPTY;
    }
}
