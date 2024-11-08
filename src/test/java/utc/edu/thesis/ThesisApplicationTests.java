package utc.edu.thesis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class ThesisApplicationTests {
    @Autowired
    PasswordEncoder b;

    @Test
    void contextLoads() {
        System.out.println(b.encode("123456")); ;
    }

}
