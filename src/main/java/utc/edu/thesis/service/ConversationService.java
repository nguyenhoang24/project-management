package utc.edu.thesis.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.CreateConversationRequest;
import utc.edu.thesis.domain.entity.Conversation;
import utc.edu.thesis.domain.entity.ConversationMember;
import utc.edu.thesis.repository.ConversationMemberRepository;
import utc.edu.thesis.repository.ConversationRepository;
import utc.edu.thesis.repository.IUserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;

    private final ConversationMemberRepository conversationMemberRepository;

    private final IUserRepo userRepository;

    @Transactional
    public Conversation startConversation(CreateConversationRequest request) {
        if ("chat-ai".equals(request.getType())) {
            return conversationRepository.findByTypeAndUser(request.getUserId())
                    .orElseGet(() -> {
                        var conversation = new Conversation();
                        conversation.setType(request.getType());
                        conversationRepository.save(conversation);

                        ConversationMember member = new ConversationMember();
                        member.setConversation(conversation);
                        member.setUser(userRepository.findById(request.getUserId())
                                .orElseThrow(() -> new RuntimeException("User not found")));
                        conversationMemberRepository.save(member);

                        return conversation;
                    });
        } else {
            return conversationRepository
                    .findByTypeAndMembers(request.getUserIds(), request.getUserIds().size())
                    .stream()
                    .findFirst()
                    .orElseGet(() -> {
                        var conversation = new Conversation();
                        conversation.setType(request.getType());
                        conversationRepository.save(conversation);

                        request.getUserIds().forEach(userId -> {
                            ConversationMember member = new ConversationMember();
                            member.setConversation(conversation);
                            member.setUser(userRepository.findById(userId)
                                    .orElseThrow(() -> new RuntimeException("User not found")));
                            conversationMemberRepository.save(member);
                        });

                        return conversation;
                    });
        }
    }

//    @Transactional
//    public Conversation startConversation(CreateConversationRequest request) {
//        // Kiểm tra xem có cuộc trò chuyện nào giữa các người dùng này đã tồn tại chưa
//        List<Conversation> existingConversations = conversationRepository
//                .findByTypeAndMembers(request.getUserIds(), request.getUserIds().size());
//
//        if (!existingConversations.isEmpty()) {
//            // Trả về cuộc trò chuyện đã tồn tại
//            return existingConversations.get(0);
//        }
//
//        // Nếu chưa có, tạo một conversation mới
//        Conversation conversation = new Conversation();
//        conversation.setType(request.getType());
//        conversationRepository.save(conversation);  // Lưu conversation trước khi thêm thành viên
//
//        // Thêm thành viên vào cuộc trò chuyện
//        for (Long userId : request.getUserIds()) {
//            ConversationMember member = new ConversationMember();
//            member.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
//            member.setConversation(conversation);  // Thiết lập conversation trước khi lưu member
//            conversation.getMembers().add(member); // Thêm vào tập members của Conversation
//            conversationMemberRepository.save(member); // Lưu từng thành viên vào cơ sở dữ liệu
//        }
//
//        // Không cần lưu lại conversation vì cascade đã tự động lưu thành viên
//
//        return conversation;
//    }

    public Conversation getConversationById(Long id) {
        if (id != null) {
            return conversationRepository.findById(id).orElse(null);
        }
        throw new IllegalArgumentException("id get conversation null");
    }
}
