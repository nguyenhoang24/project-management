package utc.edu.thesis.service.impl;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.User;
import utc.edu.thesis.exception.request.UnauthenticatedException;
//import utc.edu.thesis.security.jwt.JWTUtils;
import utc.edu.thesis.service.JwtService;
import utc.edu.thesis.security.response.AuthenticationResponse;
import utc.edu.thesis.service.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final SessionService sessionService;
    private final UserService userService;
    private final AssignmentService assignmentService;
    private final JwtService jwtService;
    private final TokenService tokenService;

    @Override
    public AuthenticationResponse createAuthenticationToken(UserRequest userRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword())
            );

        } catch (BadCredentialsException e) {
            throw new UnauthenticatedException("Incorrect username or password");
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Long userId = null;
        if (userDetails != null) {
            userId = userService.getUser(userDetails.getUsername()).getId();
        }
        final String jwt = jwtService.generateToken(userDetails);
        int expired = jwtService.getJwtExpirationInMs();
        final String jwtRefresh = jwtService.generateRefreshToken(userDetails);

        long studentId = 0, teacherId = 0, sessionId = 0;
        if (!"admin".equals(userRequest.getUsername())) {
            sessionId = sessionService.getSessionActive().getId();
        }

        String email = userService.getUser(userRequest.getUsername()).getEmail() != null
                ? userService.getUser(userRequest.getUsername()).getEmail() : "admin";
        SearchDto searchDto = new SearchDto(email, "EMAIL");
        List<TeacherDto> teachers = teacherService.getTeacher(searchDto);
        List<StudentDto> student = studentService.getStudent(searchDto);
        if (!teachers.isEmpty()) {
            teacherId = teachers.get(0).getId();
        }
        if (!student.isEmpty()) {
            studentId = student.get(0).getId();
            teacherId = assignmentService
                    .getAssign(new SearchDto("%d,%d".formatted(studentId, sessionId), "TEACHER"))
                    .get(0)
                    .getTeacher().getId();
        }
        tokenService.createToken(this.userService.getUser(userDetails.getUsername()), jwt, jwtRefresh);

        new UsernamePasswordAuthenticationToken(userRequest.getUsername(), userRequest.getPassword(), userDetails.getAuthorities());


        return new AuthenticationResponse(jwt, jwtRefresh, userDetails.getUsername(), userId, userDetails.getAuthorities(), expired, studentId, teacherId, sessionId);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        String tokenPrefix = jwtService.getTokenPrefix();
        String token = request.getHeader(AUTHORIZATION).substring(tokenPrefix.length());

        // Extract all claims from the token
        Claims claims = jwtService.extractAllClaims(token);

        // Create a new token using the claims
        String refreshedToken = jwtService.doGenerateRefreshToken(claims, claims.getSubject());

        return new AuthenticationResponse(refreshedToken);
    }

    @Override
    public BaseResponse<?> loginUser(UserRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User customUserDetails = (User) authentication.getPrincipal();
        String jwt = jwtService.generateToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);
        List<String> roleList = customUserDetails.getAuthorities().stream()
                .map(item -> item.getAuthority()).collect(Collectors.toList());

        tokenService.createToken((User) this.userService.getUserByUsername(customUserDetails.getUsername()), jwt, refreshToken);

        return BaseResponse.of(new JwtResponse(jwt, customUserDetails.getId(), customUserDetails.getUsername()
                        , customUserDetails.getEmail()
                        , roleList, refreshToken),
                200, "Login success");
    }


//    @Override
//    public AuthenticationResponse refreshToken(HttpServletRequest request) {
//        String tokenPrefix = jwtUtils.getTokenPrefix();
//        DefaultClaims claims = (DefaultClaims) jwtUtils.extractAllClaims(request.getHeader(AUTHORIZATION).substring(tokenPrefix.length()));
//
//        Map<String, Object> expectedMap = new HashMap<>(claims);
//        String token = jwtUtils.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
//        return new AuthenticationResponse(token);
//    }
}