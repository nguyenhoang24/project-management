package utc.edu.thesis.controller.admin;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import utc.edu.thesis.domain.dto.UserRequest;
import utc.edu.thesis.service.AuthenticationService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthenticationControllerTest {
    @Autowired
    AuthenticationService authenticationService;

    @Test
    void testSignIn() {
        System.out.println(authenticationService.createAuthenticationToken(new UserRequest("nthieu@utc.edu.vn", "123456")));
    }
}