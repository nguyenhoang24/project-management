package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utc.edu.thesis.domain.dto.CreateConversationRequest;
import utc.edu.thesis.domain.entity.Conversation;
import utc.edu.thesis.service.ConversationService;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @PostMapping("/start")
    public ResponseEntity<Conversation> startConversation(@RequestBody CreateConversationRequest request) {
        Conversation conversation = conversationService.startConversation(request);
        return ResponseEntity.ok(conversation);
    }


}
