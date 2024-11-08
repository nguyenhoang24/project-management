package utc.edu.thesis.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    // Lưu trữ thông tin xác thực theo session ID
    private final Map<String, UsernamePasswordAuthenticationToken> sessionAuthMap = new ConcurrentHashMap<>();

    public void putSession(String sessionId, UsernamePasswordAuthenticationToken auth) {
        sessionAuthMap.put(sessionId, auth);
    }

    public UsernamePasswordAuthenticationToken getSession(String sessionId) {
        return sessionAuthMap.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessionAuthMap.remove(sessionId);
    }
}

