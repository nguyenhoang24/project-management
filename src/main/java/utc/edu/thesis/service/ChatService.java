package utc.edu.thesis.service;

import org.springframework.http.ResponseEntity;
import utc.edu.thesis.domain.dto.BaseResponse;
import utc.edu.thesis.domain.dto.ChatMessage;
import utc.edu.thesis.domain.dto.ModelAI;

public interface ChatService {

    BaseResponse<?> createMessage(ChatMessage chatMessage);

    BaseResponse<?> getListMessagesByConversationId(Long conversationId);

    ResponseEntity<String> chatAI(ModelAI modelAI);
}
