package utc.edu.thesis.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.service.OnlineOfflineService;
import utc.edu.thesis.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    private final OnlineOfflineService onlineOfflineService;

    @PostMapping("/get-user")
    public ResponseEntity<List<UserDto>> getUser(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(userService.getUser(searchDto));
    }

    @PostMapping("/save-user")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.saveUser(userDto));
    }

    @PostMapping("/update-user")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.updateUser(userDto));
    }

    @PostMapping("/delete-user/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> deleteUser(@RequestBody UserChangePassword request) {
        return ResponseEntity.ok(userService.changePassword(request));
    }

    @GetMapping("/get-role")
    public ResponseEntity<List<Role>> getRole() {
        return ResponseEntity.ok(userService.getRoles());
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<UserResponse>> getAllUsersWithStatus() {
//        List<UserResponse> users = onlineOfflineService.getAllUsersWithStatus();
//        return ResponseEntity.ok(users);
//    }
}
