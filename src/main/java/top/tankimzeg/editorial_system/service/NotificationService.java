package top.tankimzeg.editorial_system.service;

public interface NotificationService {
    void send(String to, String subject, String content);
    void sendHtml(String to, String subject, String html);
}
