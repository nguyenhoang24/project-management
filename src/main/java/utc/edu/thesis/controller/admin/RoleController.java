package utc.edu.thesis.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.service.impl.RoleService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/get-role")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

    @PostMapping("/save-role")
    public ResponseEntity<Role> saveUser(@RequestBody Role roleDto) {
        return ResponseEntity.ok(roleService.save(roleDto));
    }


    @PostMapping("/delete-role/{id}")
    public ResponseEntity<Boolean> deleteRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.delete(id));
    }

}
