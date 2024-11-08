package utc.edu.thesis.controller.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.StudentDto;
import utc.edu.thesis.domain.dto.TeacherDto;
import utc.edu.thesis.service.teacher.ManagerStudentService;

import java.util.List;

@RestController
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class ManageStudentController {
    private final ManagerStudentService managerStudentService;

    @PostMapping("/get-student")
    public ResponseEntity<List<StudentDto>> getStudent(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(managerStudentService.getStudent(searchDto));
    }
}
