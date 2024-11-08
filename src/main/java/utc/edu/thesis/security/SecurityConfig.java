package utc.edu.thesis.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import utc.edu.thesis.security.jwt.JwtAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
//    private final CustomAuthorizationFilter authorizationFilter;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final AuthenticationProvider authenticationProvider;

    private final JwtAuthenticationFilter jwtAuthFilter;
    private static final String[] WHITE_LIST_URL = {"/signin", "/api/token/refresh", "/chat", "/chat1", "/ws/**", "users/**", "/conversations/start"};

//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        // Disable CSRF (since we are using JWT) and configure session management as stateless
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/signin", "/api/token/refresh", "/chat", "/chat", "/ws/**", "/users/**", "/conversations/start").permitAll()  // Public endpoints
//                        .requestMatchers("/api/admin/**").hasAnyAuthority("ROLE_ADMIN")  // Admin endpoints
//                        .anyRequest().authenticated()  // All other requests require authentication
//                )
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));  // Stateless session for JWT
//
//        // Add JWT authentication filter
//        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


//
//    @Bean
//    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration)
//            throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Cho phép CORS trên tất cả các endpoint
//                        .allowedOrigins("https://doantotnghiep.io.vn") // Chỉ cho phép domain này truy cập
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD") // Chỉ định các phương thức HTTP cho phép
//                        .allowedHeaders("Content-Type", "Authorization", "Content-Length", "X-Requested-With") // Cho phép các headers này
//                        .allowCredentials(true) // Cho phép gửi credentials như cookies, authorization headers hoặc TLS client certificates
//                        .maxAge(3600); // Thời gian tối đa (giây) mà trình duyệt nên lưu cache kết quả của pre-flight response
//            }
//        };
//    }
}
