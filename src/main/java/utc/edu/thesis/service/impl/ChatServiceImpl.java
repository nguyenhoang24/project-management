package utc.edu.thesis.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import utc.edu.thesis.domain.dto.BaseResponse;
import utc.edu.thesis.domain.dto.ChatMessage;
import utc.edu.thesis.domain.dto.MessageAI;
import utc.edu.thesis.domain.dto.ModelAI;
import utc.edu.thesis.domain.entity.Message;
import utc.edu.thesis.repository.ChatRepository;
import utc.edu.thesis.service.ChatService;
import utc.edu.thesis.service.ConversationService;
import utc.edu.thesis.service.UserService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {


    private final ChatRepository chatRepository;

    private final ConversationService conversationService;

    private final UserService userService;

    private final RestTemplate restTemplate;

    private final String url = "http://42.96.43.215:7003/api/chat";

    @Override
    @Transactional
    public BaseResponse<?> createMessage(ChatMessage chatMessage) {

        log.info("Full topping: {}", chatMessage.toString());

        var message = Message.builder()
                .content(chatMessage.getContent())
                .conversation(conversationService.getConversationById(chatMessage.getConversationId()))
                .user(userService.getUserById(chatMessage.getUserId()))
                .timestamp(LocalDateTime.now())
                .build();

        message = chatRepository.save(message);

        return BaseResponse.of(message, 200, "Tạo thành công tin nhắn");
    }

    @Override
    public BaseResponse<?> getListMessagesByConversationId(Long conversationId) {
        var messages = chatRepository.findByConversationId(conversationId).orElse(null);

        return BaseResponse.of(messages, 200, "Get list messages ok");
    }

    @Override
    public ResponseEntity<String> chatAI(ModelAI modelAI) {
        // Tạo request body từ modelAI
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", modelAI.getModel());
        requestBody.put("messages", modelAI.getMessages());
        requestBody.put("stream", modelAI.getStream());
        requestBody.put("temperature", modelAI.getTemperature());

        // Tạo headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Tạo HttpEntity (chứa body và headers)
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Gửi request và nhận response từ API
        ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);

        // Log status và response body (nếu cần)
        log.info("Status res: {}", response.getStatusCode());
        log.info("Request body: {}", response.getBody());

        // Trả về JSON response nhận được từ API
        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
