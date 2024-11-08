package utc.edu.thesis.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class CreateConversationRequest {
    private String type;  // '1-1' hoặc 'group'
    private List<Long> userIds;  // Danh sách ID người dùng tham gia vào cuộc trò chuyện
    private Long userId; // with user chat AI
}
