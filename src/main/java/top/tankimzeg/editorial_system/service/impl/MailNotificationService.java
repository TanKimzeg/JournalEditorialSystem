package top.tankimzeg.editorial_system.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.tankimzeg.editorial_system.service.NotificationService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
public class MailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public MailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void send(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Async
    @Override
    public void sendHtml(String to, String subject, String html) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send HTML email to {}: {}", to, e.getMessage(), e);
        }
    }
}
