package top.tankimzeg.editorial_system;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.tankimzeg.editorial_system.service.NotificationService;

@SpringBootTest
public class MailNotificationServiceTests {

    @Autowired
    NotificationService notificationService;

    @Disabled("Requires SMTP credentials; enable locally to verify sending")
    @Test
    void sendMail() {
        notificationService.send("recipient@example.com", "Test", "Hello from tests");
    }
}
