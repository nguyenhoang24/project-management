//package utc.edu.thesis.service.impl;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.social.facebook.api.Facebook;
//import org.springframework.social.facebook.api.impl.FacebookTemplate;
//import org.springframework.stereotype.Service;
//import utc.edu.thesis.domain.dto.TokenDto;
//import utc.edu.thesis.domain.entity.Role;
//import utc.edu.thesis.domain.entity.User;
//import utc.edu.thesis.repository.IRoleRepo;
//import utc.edu.thesis.repository.IUserRepo;
//import utc.edu.thesis.service.IOauthService;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Service
//@RequiredArgsConstructor
//public class OauthService implements IOauthService {
//    @Value("${secretPsw}")
//    String secretPsw;
//    private final IRoleRepo iRoleRepo;
//    private final IUserRepo iUserRepo;
//    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;
//    private final JwtService jwtService;
//
//    @Override
//    public TokenDto getTokenFacebook(TokenDto tokenDto) {
//        Facebook facebook = new FacebookTemplate(tokenDto.getValue());
//        final String [] fields = {"email"};
//        org.springframework.social.facebook.api.User user = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
//        String email = user.getEmail();
//        User userFace = new User();
//        if(iUserRepo.existsByUsername(email)){
//            userFace = iUserRepo.findByUsername(email);
//        } else {
//            userFace = createUser(email);
//        }
//        return login(userFace);
//    }
//
//    public TokenDto login(User user) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(user.getUsername(), secretPsw));
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        String jwt = jwtService.generateTokenLogin(authentication);
//        TokenDto tokenDto = new TokenDto();
//        tokenDto.setValue(jwt);
//        return tokenDto;
//    }
//
//    public User createUser(String email) {
//        User user = new User();
//        user.setUsername(email);
//        user.setPassword(passwordEncoder.encode(secretPsw));
//        if (user.getRoles() == null) {
//            Role role1 = iRoleRepo.findByName("ROLE_USER");
//            Set<Role> roles1 = new HashSet<>();
//            roles1.add(role1);
//            user.setRoles(roles1);
//        }
//        user.setAvatar("https://hocban.vn/wp-content/uploads/2018/05/avatar-dep-nhat-33_112147.jpg");
//        user.setConfirmPassword(passwordEncoder.encode(secretPsw));
//        return iUserRepo.save(user);
//    }
//}
