package org.typecrafters.teambuild.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.typecrafters.teambuild.domain.exception.AppException;

@Service
public class MailServiceImpl implements MailService {
    private final TemplateEngine templateEngine;
    private final JavaMailSender mailSender;

    public MailServiceImpl(TemplateEngine templateEngine, JavaMailSender mailSender) {
        this.templateEngine = templateEngine;
        this.mailSender = mailSender;
    }

    @Override
    public void sendText(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    @Override
    public void sendHtml(String to, String subject, String htmlBody) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email to " + to, e);
        }
    }

    @Override
    public void sendThymeleaf(
            String to,
            String subject,
            String templatePath,
            Map<String, Object> data) {
        Context context = new Context();

        data.entrySet().forEach(entry -> {
            context.setVariable(entry.getKey(), entry.getValue());
        });

        try {
            String html = templateEngine.process(templatePath, context);
            sendHtml(to, subject, html);
        } catch (TemplateInputException e) {
            throw AppException.internalServerError(
                    "Email template '" + templatePath + "' does not exist.");
        }
    }
}
