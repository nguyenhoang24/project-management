package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import utc.edu.thesis.domain.dto.ChatMessage;
import utc.edu.thesis.domain.dto.ModelAI;
import utc.edu.thesis.domain.entity.Message;
import utc.edu.thesis.service.ChatService;
import utc.edu.thesis.service.OnlineOfflineService;
import utc.edu.thesis.service.UserService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling chat-related functionality.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserService userService;

    private final ChatService chatService;

    private final OnlineOfflineService onlineOfflineService;

    /**
     * Registers a user for chat.
     * 
     * param chatMessage The chat message containing the sender's information.
     * param headerAccessor The SimpMessageHeaderAccessor object used to access session attributes.
     * return The registered chat message.
     */
//    @MessageMapping("/chat.register")
//    @SendTo("/topic/public")
//    public ChatMessage register(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
//
//        // Kiểm tra nếu chatMessage có conversationId để tham gia vào cuộc trò chuyện riêng tư hoặc nhóm
//        if (chatMessage.getConversationId() != null) {
//            headerAccessor.getSessionAttributes().put("conversationId", chatMessage.getConversationId());
//            chatMessage.setContent("User " + chatMessage.getSender() + " has joined conversation " + chatMessage.getConversationId());
//        } else {
//            chatMessage.setContent("User " + chatMessage.getSender() + " has joined the public chat");
//        }
//
//        return chatMessage;
//    }

    /**
     * Sends a chat message to all connected users.
     * 
     * param chatMessage The chat message to be sent.
     * return The sent chat message.
     */



    @MessageMapping("/chat/{conversationId}/sendMessage")
    @SendTo("/topic/conversation/{conversationId}/newMessage")
    public ChatMessage sendMessage(
            @DestinationVariable String conversationId,
            @Payload ChatMessage chatMessage,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        headerAccessor.getSessionAttributes().put("conversationId", conversationId);

        log.info("??CSDCDCDCD {}", chatMessage.getChatType());
        chatService.createMessage(chatMessage);

//        onlineOfflineService.notifyGroupMembers(chatMessage.getSender(), Long.parseLong(conversationId));
//        if ("CHAT-AI".equals(chatMessage.getChatType())) {
//            chatService.chatAI(chatMessage);
//        }

        log.info("tôi log: {}", headerAccessor.getUser());
        return chatMessage;
    }

    @MessageMapping("/chat/{conversationId}/getMessages")
    @SendTo("/topic/conversation/{conversationId}/messages")
    public List<Message> getMessages(@DestinationVariable Long conversationId) {
        try {
            var listMessages = chatService.getListMessagesByConversationId(conversationId);

            log.info("message đây {}", listMessages);

            // Kiểm tra null và kiểu dữ liệu của listMessages.getResult()
            if (listMessages == null || listMessages.getResult() == null) {
                log.error("No messages found for conversationId: {}", conversationId);
                return new ArrayList<>();
            }

            return (List<Message>) listMessages.getResult();
        } catch (Exception e) {
            log.error("Error while retrieving messages for conversationId: " + conversationId, e);
            throw new RuntimeException("Error retrieving messages for conversation: " + conversationId);
        }
    }

}
