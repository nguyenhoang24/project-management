package utc.edu.thesis.controller.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.TeacherDto;
import utc.edu.thesis.service.TeacherService;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class RegulationController {
    private final TeacherService teacherService;

    @PostMapping("/add-regulation")
    public ResponseEntity<String> getTeacher(@RequestBody TeacherDto payload) {
        return ResponseEntity.ok(teacherService.addRegulation(payload));
    }
}
