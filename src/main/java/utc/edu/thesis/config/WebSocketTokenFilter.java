package utc.edu.thesis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import utc.edu.thesis.service.JwtService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class WebSocketTokenFilter implements ChannelInterceptor {


    private final JwtService jwtUtils;
    private final UserDetailsService userDetailsService;

    private final WebSocketSessionManager sessionManager;

    private UsernamePasswordAuthenticationToken cachedAuthentication = null;

    public WebSocketTokenFilter(JwtService jwtUtils, UserDetailsService userDetailsService, WebSocketSessionManager sessionManager) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.sessionManager = sessionManager;
    }

//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        // Lấy token từ tiêu đề
//        String token = accessor.getFirstNativeHeader("Authorization");
//
//        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Xác thực token và thiết lập Principal
//            String username = jwtUtils.extractUsername(token.substring(7));
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if (jwtUtils.isTokenValid(token.substring(7), userDetails)) {
//                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                log.info("????????????????????????? {}", auth.getPrincipal());
//                accessor.setUser((Principal) auth.getPrincipal());
//                SecurityContextHolder.getContext().setAuthentication(auth);
//            }
//        }
//
//        return message;
//    }

    private final Map<String, UsernamePasswordAuthenticationToken> sessionAuthMap = new ConcurrentHashMap<>();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();

        synchronized(this) {
            if (accessor.getCommand() != null) {
                if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
                    String token = accessor.getFirstNativeHeader("Authorization");

                    if (token != null) {
                        try {
                            String username = jwtUtils.extractUsername(token.substring(7));
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            if (jwtUtils.isTokenValid(token.substring(7), userDetails)) {
                                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                                accessor.setUser(auth);
                                SecurityContextHolder.getContext().setAuthentication(auth);

                                sessionAuthMap.put(sessionId, auth);
                                sessionManager.putSession(sessionId, auth);
                                log.info("User authenticated: {}", username);
                            } else {
                                log.warn("Invalid token for user: {}", username);
                            }
                        } catch (Exception e) {
                            log.error("Error processing WebSocket authentication: {}", e.getMessage());
                        }
                    }
                } else if (sessionAuthMap.containsKey(sessionId)) {
                    UsernamePasswordAuthenticationToken auth = sessionAuthMap.get(sessionId);
                    accessor.setUser(auth);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    log.info("Using cached authentication for user: {}", auth.getName());
                }
            }
        }

        return message;
    }




    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
        sessionAuthMap.remove(sessionId); // Xóa thông tin xác thực khi người dùng ngắt kết nối
        log.info("User disconnected, session ID: {}", sessionId);
    }


}
