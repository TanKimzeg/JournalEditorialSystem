package top.tankimzeg.editorial_system;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import top.tankimzeg.editorial_system.service.NotificationService;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MailNotificationIntegrationTest {

    static GreenMail greenMail;

    @DynamicPropertySource
    static void mailProps(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> 3025);
        registry.add("spring.mail.username", () -> "test@local");
        registry.add("spring.mail.password", () -> "password");
        registry.add("spring.mail.properties.mail.smtp.auth", () -> true);
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> false);
    }

    @BeforeEach
    void startServer() {
        greenMail = new GreenMail(new ServerSetup(3025, null, ServerSetup.PROTOCOL_SMTP));
        greenMail.start();
        // Create matching user for authentication
        greenMail.setUser("test@local", "password");
    }

    @AfterEach
    void stopServer() {
        if (greenMail != null) {
            greenMail.stop();
        }
    }

    @Autowired
    NotificationService notificationService;

    @Test
    void sendPlainTextMail() throws Exception {
        notificationService.send("receiver@local", "Plain", "Hello");
        // Wait for incoming async mail
        boolean arrived = greenMail.waitForIncomingEmail(5000, 1);
        assertThat(arrived).isTrue();
        var received = greenMail.getReceivedMessages();
        assertThat(received).isNotNull();
        assertThat(received.length).isGreaterThanOrEqualTo(1);
        assertThat(received[0].getSubject()).isEqualTo("Plain");
    }

    @Test
    void sendHtmlMail() throws Exception {
        notificationService.sendHtml("receiver@local", "HTML", "<p>Hello</p>");
        boolean arrived = greenMail.waitForIncomingEmail(5000, 1);
        assertThat(arrived).isTrue();
        var received = greenMail.getReceivedMessages();
        assertThat(received[received.length - 1].getSubject()).isEqualTo("HTML");
    }
}
