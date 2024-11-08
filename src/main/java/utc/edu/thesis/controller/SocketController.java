package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utc.edu.thesis.domain.dto.UserResponse;
import utc.edu.thesis.service.OnlineOfflineService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SocketController {

    private final OnlineOfflineService onlineOfflineService;

    @GetMapping("/users/list-user-chat/{userId}")
    public ResponseEntity<List<UserResponse>> getAllUsersWithStatus(@PathVariable String userId) {
        Long id = Long.parseLong(userId);
        System.out.println("?????????????????????  "  + id.getClass().getName());
        List<UserResponse> users = onlineOfflineService.getAllUsersWithoutUserLoginWithStatus(id);
        return ResponseEntity.ok(users);
    }
}
