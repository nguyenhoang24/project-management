package utc.edu.thesis.controller.admin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import utc.edu.thesis.domain.dto.BaseResponse;
import utc.edu.thesis.domain.dto.UserRequest;
import utc.edu.thesis.security.response.AuthenticationResponse;
import utc.edu.thesis.service.AuthenticationService;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping(value = "/signin")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(service.createAuthenticationToken(userRequest));
    }

    @GetMapping(value = "/token/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        return ResponseEntity.ok(service.refreshToken(request));
    }

//    @PostMapping("/signin")
//    public ResponseEntity<BaseResponse> loginUser(@RequestBody UserRequest request) {
//        return ResponseEntity.ok(this.service.loginUser(request));
//    }
}
