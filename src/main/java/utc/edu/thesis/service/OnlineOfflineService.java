package utc.edu.thesis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.ChatMessage;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.UserConnection;
import utc.edu.thesis.domain.dto.UserResponse;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.Teacher;
import utc.edu.thesis.domain.entity.User;
import utc.edu.thesis.repository.*;

import java.security.Principal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

@Slf4j
@Service
public class OnlineOfflineService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final Set<String> onlineUsers;
    private final Map<String, Set<String>> userSubscribed;
    private final IUserRepo userRepository;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ConversationRepository conversationRepository;

    private final TeacherService teacherService;

    private final StudentService studentService;

    private final AssignmentRepository assignmentRepository;



    public OnlineOfflineService(
            IUserRepo userRepository, SimpMessageSendingOperations simpMessageSendingOperations, ConversationRepository conversationRepository, TeacherService teacherService, StudentService studentService, AssignmentRepository assignmentRepository,
            StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.conversationRepository = conversationRepository;
        this.teacherService = teacherService;
        this.studentService = studentService;
        this.assignmentRepository = assignmentRepository;
        this.teacherRepository = teacherRepository;
        this.onlineUsers = new ConcurrentSkipListSet<>();
        this.userSubscribed = new ConcurrentHashMap<>();
        this.userRepository = userRepository;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
        this.studentRepository = studentRepository;
    }

    public void addOnlineUser(Principal user) {
        if (user == null) return;

        UserDetails userDetails = getUserDetails(user);
        String username = userDetails.getUsername();

        log.info("{} is online", username);

        for (String id : onlineUsers) {
            if (!id.equals(username)) {  // Không gửi thông báo cho chính mình
                String destination = "/topic/users/" + id + "/notifications";
                log.info("Sending notification to: {}", destination);
                simpMessageSendingOperations.convertAndSend(
                        destination,
                        ChatMessage.builder()
                                .type(ChatMessage.MessageType.FRIEND_ONLINE)
                                .userConnection(UserConnection.builder().username(username).build())
                                .build());
            }
        }

        if (!onlineUsers.contains(userDetails.getUsername())) {
            onlineUsers.add(userDetails.getUsername());
        }
        log.info("Current online users: {}", onlineUsers);
    }

    public void removeOnlineUser(Principal user) {
        if (user != null) {
            UserDetails userDetails = getUserDetails(user);
            log.info("{} went offline", userDetails.getUsername());
            onlineUsers.remove(userDetails.getUsername());
            userSubscribed.remove(userDetails.getUsername());
            for (String id : onlineUsers) {
                simpMessageSendingOperations.convertAndSend(
                        "/topic/" + id,
                        ChatMessage.builder()
                                .type(ChatMessage.MessageType.FRIEND_ONLINE)
                                .userConnection(UserConnection.builder().username(userDetails.getUsername()).build())
                                .build());
            }
        }
    }

    public boolean isUserOnline(String username) {
        return onlineUsers.contains(username);
    }

    private UserDetails getUserDetails(Principal principal) {
        if (principal == null) {
            log.error("Principal is null");
            return null;
        }

        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
            log.error("Principal is not an instance of UsernamePasswordAuthenticationToken: {}", principal);
            return null;
        }

        UsernamePasswordAuthenticationToken user = (UsernamePasswordAuthenticationToken) principal;
        Object object = user.getPrincipal();

        if (object instanceof UserDetails) {
            return (UserDetails) object;
        } else {
            log.error("Principal is not an instance of UserDetails: {}", object);
            return null;
        }
    }

//    public List<UserResponse> getOnlineUsers() {
//        return userRepository.findAllById(onlineUsers).stream()
//                .map(userEntity -> UserResponse.toDto(userEntity))
//                .toList();
//    }

    public List<UserResponse> getAllUsersWithoutUserLoginWithStatus(Long userId) {
        Set<User> users = new HashSet<>();
        User userLg = userRepository.findById(userId).orElse(null);

        SearchDto dto = new SearchDto();
        dto.setConditionSearch("EMAIL");
        dto.setValueSearch(userLg.getEmail());

        if (userLg != null) {
            var userAdmin = userRepository.findById(1L);
            if (userAdmin.isPresent()) {
                users.add(userAdmin.get());
            }

            Set<Role> roles = userLg.getRoles();
            boolean hasRoleAdmin = roles.stream()
                    .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

            boolean hasRoleTeacher = roles.stream()
                            .anyMatch(role -> "ROLE_TEACHER".equals(role.getName()));

            if (hasRoleTeacher && !hasRoleAdmin) {
                //teacher ở đây
                var teacher = teacherService.getTeacher(dto);
                var assignments = assignmentRepository.findByTeacherId(teacher.get(0).getId()).orElse(null);

                List<Student> students = new ArrayList<>();
                assignments.forEach(
                    assignment -> {
                        var student = studentRepository.findById(assignment.getStudent().getId()).orElse(null);

                        if (student != null) {
                            students.add(student);
                        }
                    }
                );

                students.forEach(
                        student -> {
                            var user = userRepository.findByEmail(student.getEmail());

                            if (user.isPresent()) {
                                users.add(user.get());
                            }
                        }
                );
            } else if (hasRoleAdmin) {
                List<Student> students = studentService.getAll();

                students.forEach(
                        student -> {
                            var user = userRepository.findByEmail(student.getEmail());

                            if (user.isPresent()) {
                                users.add(user.get());
                            }
                        }
                );

                List<Teacher> teachers = teacherService.getAll();

                teachers.forEach(
                        teacher -> {
                            var user = userRepository.findByEmail(teacher.getEmail());

                            if (user.isPresent()) {
                                users.add(user.get());
                            }
                        }
                );

                users.remove(userAdmin.get());
            } else {
                //student ở đây
                var student = studentService.getStudent(dto);
                var assignment = assignmentRepository.findByStudentId(student.get(0).getId()).orElse(null);

                var teacher = teacherRepository.findById(assignment.getTeacher().getId());

                if (teacher.isPresent()) {
                    var user = userRepository.findByEmail(teacher.get().getEmail());

                    if (user.isPresent()) {
                        users.add(user.get());
                    }
                }
            }

        }
        return users.stream()
                .sorted(Comparator.comparing(user -> {
                    boolean isTeacher = user.getRoles().stream()
                            .anyMatch(role -> "ROLE_TEACHER".equals(role.getName()));
                    return isTeacher ? 0 : 1;
                }))
                .map(user -> {
                    UserResponse userResponse = UserResponse.toDto(user);
                    userResponse.setOnline(onlineUsers.contains(String.valueOf(user.getUsername())));

                    Set<Role> roles = user.getRoles();
                    boolean hasRoleAdmin = roles.stream()
                            .anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));

                    boolean hasRoleTeacher = roles.stream()
                            .anyMatch(role -> "ROLE_TEACHER".equals(role.getName()));

                    var fullName = "";
                    SearchDto dtoS = new SearchDto();
                    dtoS.setConditionSearch("EMAIL");
                    dtoS.setValueSearch(user.getEmail());

                    if (hasRoleTeacher && !hasRoleAdmin) {
                        var teacher = teacherService.getTeacher(dtoS).get(0);
                        fullName = "Giảng viên " + teacher.getFullName();
                    } else if (!hasRoleAdmin){
                        var student = studentService.getStudent(dtoS).get(0); // viết lại repo
                        fullName = student.getFullName();
                    }
                    if (user.getUsername().equals("admin")) {
                        fullName = "Quản trị viên";
                    }

                    userResponse.setFullName(fullName);
                    return userResponse;
                })
                .toList();
    }



//    public void addUserSubscribed(Principal user, String subscribedChannel) {
//        UserDetails userDetails = getUserDetails(user);
//        if (userDetails == null) return;
//
//        log.info("{} subscribed to {}", userDetails.getUsername(), subscribedChannel);
//        Set<String> subscriptions = userSubscribed.getOrDefault(userDetails.getUsername(), new HashSet<>());
//        subscriptions.add(subscribedChannel);
//        userSubscribed.put(userDetails.getUsername(), subscriptions);
//    }

    public void addUserSubscribed(Principal user, String subscribedChannel) {
        // Nếu Principal null, lấy từ SecurityContextHolder
        if (user == null) {
            user = SecurityContextHolder.getContext().getAuthentication();
        }

        // Kiểm tra nếu Principal là AnonymousAuthenticationToken
        if (user instanceof AnonymousAuthenticationToken) {
            log.error("Principal is not authenticated. Unable to subscribe to channel: {}", subscribedChannel);
            return;
        }

        // Chỉ xử lý nếu Principal là UsernamePasswordAuthenticationToken
        if (!(user instanceof UsernamePasswordAuthenticationToken)) {
            log.error("Principal is not an instance of UsernamePasswordAuthenticationToken: {}", user);
            return;
        }

        UserDetails userDetails = getUserDetails(user);
        if (userDetails == null) {
            log.error("UserDetails is null for Principal, unable to subscribe to channel: {}", subscribedChannel);
            return;
        }

        log.info("{} subscribed to {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscriptions = userSubscribed.getOrDefault(userDetails.getUsername(), new HashSet<>());
        subscriptions.add(subscribedChannel);
        userSubscribed.put(userDetails.getUsername(), subscriptions);
    }



    public void removeUserSubscribed(Principal user, String subscribedChannel) {
        UserDetails userDetails = getUserDetails(user);
        if (userDetails == null) return;

        log.info("unsubscription! {} unsubscribed {}", userDetails.getUsername(), subscribedChannel);
        Set<String> subscriptions = userSubscribed.getOrDefault(userDetails.getUsername(), new HashSet<>());
        subscriptions.remove(subscribedChannel);
        userSubscribed.put(userDetails.getUsername(), subscriptions);
    }

    public boolean isUserSubscribed(UUID username, String subscription) {
        Set<String> subscriptions = userSubscribed.getOrDefault(username, new HashSet<>());
        return subscriptions.contains(subscription);
    }

//    public Map<String, Set<String>> getUserSubscribed() {
//        Map<String, Set<String>> result = new HashMap<>();
//        List<User> users = userRepository.findByUsername(userSubscribed.keySet());
//        users.forEach(user -> result.put(user.getUsername(), userSubscribed.get(user.getId())));
//        return result;
//    }

//    public void notifySender(
//            String username,
//            List<Conversation> entities,
//            MessageDeliveryStatusEnum messageDeliveryStatusEnum) {
//        if (!isUserOnline(username)) {
//            log.info(
//                    "{} is not online. cannot inform the socket. will persist in database",
//                    username);
//            return;
//        }
//        List<MessageDeliveryStatusUpdate> messageDeliveryStatusUpdates =
//                entities.stream()
//                        .map(
//                                e ->
//                                        MessageDeliveryStatusUpdate.builder()
//                                                .id(e.getId())
//                                                .messageDeliveryStatusEnum(messageDeliveryStatusEnum)
////                                                .content(e.getContent())
//                                                .build())
//                        .toList();
//        for (Conversation entity : entities) {
//            simpMessageSendingOperations.convertAndSend(
//                    "/topic/" + username,
//                    ChatMessage.builder()
//                            .id(entity.getId())
//                            .messageDeliveryStatusUpdates(messageDeliveryStatusUpdates)
//                            .type(ChatMessage.MessageType.MESSAGE_DELIVERY_UPDATE)
////                            .content(entity.getContent())
//                            .build());
//        }
//    }


//    public void notifyGroupMembers(String senderUsername, Long conversationId) {
//        // Truy vấn conversation từ repository
//        var conversation = conversationRepository.findByIdWithMembers(conversationId).orElseThrow(
//                () -> new EntityNotFoundException("Conversation with id = %s not found".formatted(conversationId))
//        );
//
//        // Kiểm tra nếu members là null
//        if (conversation.getMembers() == null) {
//            throw new IllegalStateException("Members not found for conversation with id = " + conversationId);
//        }
//
//        // Loại bỏ người gửi khỏi danh sách các thành viên
//        var groupMembers = conversation.getMembers()
//                .stream()
//                .filter(member -> !member.getUser().getUsername().equals(senderUsername))  // Loại bỏ người gửi khỏi danh sách
//                .toList();
//
//        // Tạo thông báo và gửi tới từng thành viên trong nhóm
//        ChatMessage chatMessage = ChatMessage.builder()
//                .id(conversation.getId())
//                .sender(senderUsername)
//                .type(ChatMessage.MessageType.MESSAGE_DELIVERY_UPDATE)
//                .build();
//
//        // Gửi thông báo tới tất cả các thành viên trong nhóm
//        for (ConversationMember member : groupMembers) {
//            simpMessageSendingOperations.convertAndSend(
//                    "/topic/conversation/" + member.getUser().getUsername() + "/groupNotifications",
//                    chatMessage
//            );
//        }
//    }

}
