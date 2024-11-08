package utc.edu.thesis.service;

import utc.edu.thesis.domain.dto.BaseResponse;
import utc.edu.thesis.domain.dto.UserRequest;
import utc.edu.thesis.security.response.AuthenticationResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    AuthenticationResponse createAuthenticationToken(UserRequest userRequest);

    AuthenticationResponse refreshToken(HttpServletRequest request);

    BaseResponse<?> loginUser(UserRequest request);
}
