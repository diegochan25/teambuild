package org.typecrafters.teambuild.service;

import java.util.Map;

public interface MailService {
    void sendText(String to, String subject, String body);
    void sendHtml(String to, String subject, String htmlBody);
    void sendThymeleaf(String to, String subject, String templatePath, Map<String, Object> data);
}
