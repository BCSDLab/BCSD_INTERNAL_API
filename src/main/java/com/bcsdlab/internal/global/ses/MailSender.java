package com.bcsdlab.internal.global.ses;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MailSender {

    public static final String PASSWORD_RESET_SUBJECT = "비밀번호 초기화";

    private static final String FROM = "no-reply@bcsdlab.com";

    private final SpringTemplateEngine templateEngine;
    private final AmazonSimpleEmailService amazonSimpleEmailService;

    public void send(String title, String receiver, String certificationCode) {
        sendSesMail(
            title,
            FROM,
            receiver,
            contextBy(receiver, certificationCode)
        );
    }

    private String contextBy(String receiver, String certificationCode) {
        LocalDateTime now = LocalDateTime.now();
        Context context = new Context();
        context.setVariable("certificationCode", certificationCode);
        context.setVariable("emailAddress", receiver);
        context.setVariable("year", now.getYear());
        context.setVariable("month", now.getMonth().getValue());
        context.setVariable("dayOfMonth", now.getDayOfMonth());
        context.setVariable("hour", now.getHour());
        context.setVariable("minute", now.getMinute());

        return templateEngine.process("mail/password-reset.html", context);
    }

    private void sendSesMail(String title, String from, String receiver, String html) {
        SendEmailRequest request = new SendEmailRequest()
            .withDestination(
                new Destination().withToAddresses(receiver)
            )
            .withSource(from)
            .withMessage(new Message()
                .withBody(new Body()
                    .withHtml(new Content()
                        .withCharset("UTF-8").withData(html)
                    )
                )
                .withSubject(new Content()
                    .withCharset("UTF-8").withData(title)
                )
            );

        amazonSimpleEmailService.sendEmail(request);
    }
}
