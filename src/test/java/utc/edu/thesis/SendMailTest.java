package utc.edu.thesis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import utc.edu.thesis.service.impl.EmailServiceImpl;

@SpringBootTest
public class SendMailTest {
    @Autowired
    private JavaMailSender emailSender;
    @Test
    void contextLoads() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("emailfortest612@gmail.com");
        message.setTo("trunghieu.ngo612@gmail.com");
        message.setSubject("test");
        message.setText("test");
        emailSender.send(message);
    }
}
