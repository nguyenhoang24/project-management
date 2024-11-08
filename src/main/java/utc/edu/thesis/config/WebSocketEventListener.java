package utc.edu.thesis.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import utc.edu.thesis.domain.dto.ChatMessage;
import utc.edu.thesis.service.OnlineOfflineService;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
    * Handles the WebSocket disconnect event.
    * Sends a leave message to the "/topic/public" destination when a user disconnects.
    *
    * param event The SessionDisconnectEvent representing the WebSocket disconnect event.
    */
@Component
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    private final OnlineOfflineService onlineOfflineService;

    private final Map<String, String> simpSessionIdToSubscriptionId;

    private final WebSocketSessionManager sessionManager;

    public WebSocketEventListener(SimpMessageSendingOperations messagingTemplate, OnlineOfflineService onlineOfflineService, WebSocketSessionManager sessionManager) {
        this.messagingTemplate = messagingTemplate;
        this.onlineOfflineService = onlineOfflineService;
        this.sessionManager = sessionManager;
        this.simpSessionIdToSubscriptionId = new ConcurrentHashMap<>();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if (username != null) {
            log.info("user disconnected: {}", username);
            var chatMessage = ChatMessage.builder()
                    .type(ChatMessage.MessageType.LEAVE)
                    .sender(username)
                    .build();
//            messagingTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }

    @EventListener
    @SendToUser
    public void handleSubscribeEvent(SessionSubscribeEvent sessionSubscribeEvent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
        log.info("Headers in SessionSubscribeEvent: {}", sessionSubscribeEvent.getMessage().getHeaders());
        // Lấy Principal từ SecurityContextHolder
        Principal userPrincipal = SecurityContextHolder.getContext().getAuthentication();

        if (userPrincipal == null) {
            log.error("Principal is null in SessionSubscribeEvent");
            return;
        }

        String subscribedChannel =
                (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpDestination");
        String simpSessionId =
                (String) sessionSubscribeEvent.getMessage().getHeaders().get("simpSessionId");
        if (subscribedChannel == null) {
            log.error("SUBSCRIBED TO NULL?? WAT?!");
            return;
        }
        log.info("okkkkkkkkkkkkkkkkkkkkk");
        simpSessionIdToSubscriptionId.put(simpSessionId, subscribedChannel);
        onlineOfflineService.addUserSubscribed(sessionSubscribeEvent.getUser(), subscribedChannel);
    }

    @EventListener
    public void handleUnSubscribeEvent(SessionUnsubscribeEvent unsubscribeEvent) {
        String simpSessionId = (String) unsubscribeEvent.getMessage().getHeaders().get("simpSessionId");
        String unSubscribedChannel = simpSessionIdToSubscriptionId.get(simpSessionId);
        onlineOfflineService.removeUserSubscribed(unsubscribeEvent.getUser(), unSubscribedChannel);
    }

    @EventListener
    public void handleConnectedEvent(SessionConnectedEvent sessionConnectedEvent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());
        String sessionId = accessor.getSessionId();

        // Lấy thông tin xác thực từ sessionManager
        UsernamePasswordAuthenticationToken auth = sessionManager.getSession(sessionId);

        if (auth == null) {
            log.debug("No user authentication found for WebSocket connection");
        } else {
            log.info("User connected: {}", auth);
            onlineOfflineService.addOnlineUser(auth);
        }

    }

}
