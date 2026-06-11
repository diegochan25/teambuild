package org.typecrafters.teambuild.service;

public interface MailService {
    void sendText(String to, String subject, String body);
    void sendHtml(String to, String subject, String htmlBody);
}
