//package utc.edu.thesis.service.impl;
//
//
//import io.jsonwebtoken.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import utc.edu.thesis.domain.entity.UserPrinciple;
//
//import java.util.Date;
//
//
//@Component
//@Service
//public class JwtService {
//
//    private static final String SECRET_KEY = "11111111111111111111111111111111";
//
//    private static final long EXPIRE_TIME = 86400000000L;
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class.getName());
//
//    public String generateTokenLogin(Authentication authentication) {
//        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
//
//        return Jwts.builder()
//                .setSubject((userPrinciple.getUsername()))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + EXPIRE_TIME * 1000))
//                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
//                .compact();
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken);
//            return true;
//        } catch (SignatureException e) {
//            LOGGER.error("Invalid JWT signature -> Message: {} ", e);
//        } catch (MalformedJwtException e) {
//            LOGGER.error("Invalid JWT token -> Message: {}", e);
//        } catch (ExpiredJwtException e) {
//            LOGGER.error("Expired JWT token -> Message: {}", e);
//        } catch (UnsupportedJwtException e) {
//            LOGGER.error("Unsupported JWT token -> Message: {}", e);
//        } catch (IllegalArgumentException e) {
//            LOGGER.error("JWT claims string is empty -> Message: {}", e);
//        }
//
//        return false;
//    }
//
//    public String getUserNameFromJwtToken(String token) {
//
//        String userName = Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody().getSubject();
//        return userName;
//    }
//
//
//}
