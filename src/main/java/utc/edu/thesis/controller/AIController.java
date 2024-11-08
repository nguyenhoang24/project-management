package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import utc.edu.thesis.domain.dto.ModelAI;
import utc.edu.thesis.service.ChatService;

@RestController
@RequiredArgsConstructor
public class AIController {

    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<?> chatAI(@RequestBody ModelAI modelAI) {
        return chatService.chatAI(modelAI);
    }
}
