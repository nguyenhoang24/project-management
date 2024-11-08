package utc.edu.thesis.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.SessionDto;
import utc.edu.thesis.service.SessionService;

import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
public class SessionController {
    private final SessionService sessionService;

    @PostMapping("/add-session")
    public ResponseEntity<SessionDto> addSession(@RequestBody SessionDto dto) {
        return ResponseEntity.ok(sessionService.addSession(dto));
    }

    @PostMapping("/delete-session/{id}")
    public ResponseEntity<Boolean> deleteSession(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.deleteSession(id));
    }

    @PostMapping("/edit-session")
    public ResponseEntity<SessionDto> editSession(@RequestBody SessionDto dto) {
        return ResponseEntity.ok(sessionService.editSession(dto));
    }

    @PostMapping("/get-session")
    public ResponseEntity<List<SessionDto>> getSession(@RequestBody SearchDto payload) {
        return ResponseEntity.ok(sessionService.getSession(payload));
    }

    @PostMapping("/change-status")
    public ResponseEntity<Boolean> getSession(@RequestBody SessionDto sessionDto) {
        return ResponseEntity.ok(sessionService.changeStatus(sessionDto));
    }
}
