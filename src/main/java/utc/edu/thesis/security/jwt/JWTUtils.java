//package utc.edu.thesis.security.jwt;
//
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.security.Key;
//import java.util.*;
//import java.util.function.Function;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class JWTUtils {
//    private final JWTConfig jwtConfig;
//
//    @Value("${jwt.secret}")
//    private String JWT_SECRET;
//
//    public int getJwtExpirationInMs() {
//        return jwtConfig.getExpirationDateInMs();
//    }
//
//    public int getRefreshExpirationDateInMs() {
//        return jwtConfig.getRefreshExpirationDateInMs();
//    }
//
//    public String getTokenPrefix() {
//        return jwtConfig.getTokenPrefix();
//    }
//
//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private <T> T extractClaim(String token, Function<Claims, T> claimsResolve) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolve.apply(claims);
//    }
//
//    public Claims extractAllClaims(String token) {
//        try {
//            return Jwts
//                    .parser()
//                    .setSigningKey(getSignInKey())
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (JwtException e) {
//            log.error("Error extracting claims from token", e);
//            throw e;
//        }
//    }
//
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(JWT_SECRET);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//
//    public Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
////    public String getUsernameFromToken(String token) {
////        Claims claims = Jwts.parserBuilder().setSigningKey(jwtConfig.getSecretKey()).build().parseClaimsJws(token).getBody();
////        return claims.getSubject();
////
////    }
//
////    public List<SimpleGrantedAuthority> getRolesFromToken(String token) {
////        Claims claims = Jwts.parserBuilder().setSigningKey(jwtConfig.getSecretKey()).build().parseClaimsJws(token).getBody();
////
////        List<SimpleGrantedAuthority> roles = null;
////
////        Boolean isAdmin = claims.get("isAdmin", Boolean.class);
////        Boolean isUser = claims.get("isUser", Boolean.class);
////
////        if (isAdmin != null && isAdmin) {
////            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
////        }
////
////        if (isUser != null && isAdmin) {
////            roles = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
////        }
////        return roles;
////
////    }
//
//    public String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//
//        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
//
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            claims.put("isAdmin", true);
//        }
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_USER"))) {
//            claims.put("isUser", true);
//        }
//
//        return doGenerateToken(claims, userDetails.getUsername());
//    }
//
//    public String generateRefreshToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//
//        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();
//
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
//            claims.put("isAdmin", true);
//        }
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_TEACHER"))) {
//            claims.put("isTeacher", true);
//        }
//        if (roles.contains(new SimpleGrantedAuthority("ROLE_STUDENT"))) {
//            claims.put("isStudent", true);
//        }
//
//        return doGenerateRefreshToken(claims, userDetails.getUsername());
//    }
//
//
//    private String doGenerateToken(Map<String, Object> claims, String subject) {
//
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationDateInMs()))
//                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS512).compact();
//
//    }
//
//    public String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpirationDateInMs()))
//                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS256).compact();
//    }
//
//    public String doGenerateRefreshToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpirationDateInMs()))
//                .signWith(jwtConfig.getSecretKey(), SignatureAlgorithm.HS512).compact();
//
//    }
//
//    public boolean validateToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).build().parseClaimsJws(authToken);
//            return true;
//        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
//            throw new BadCredentialsException("INVALID_CREDENTIALS", ex);
//        } catch (ExpiredJwtException ex) {
//            throw ex;
//        }
//    }
//
//    public String parseJwt(StompHeaderAccessor accessor) {
//        List<String> authorizationHeaders = accessor.getNativeHeader("Authorization");
//        if (authorizationHeaders != null && !authorizationHeaders.isEmpty()) {
//            String bearerToken = authorizationHeaders.get(0);
//            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//                return bearerToken.substring(7); // Remove "Bearer " prefix
//            }
//        }
//        return null;
//    }
//}
