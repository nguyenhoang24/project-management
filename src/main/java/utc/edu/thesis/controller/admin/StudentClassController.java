package utc.edu.thesis.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.StudentDto;
import utc.edu.thesis.domain.entity.StudentClass;
import utc.edu.thesis.service.StudentClassService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StudentClassController {
    private final StudentClassService service;

    @GetMapping("/get-course")
    public ResponseEntity<List<Integer>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping("/get-class")
    public ResponseEntity<List<StudentClass>> getStudentClass(@RequestBody SearchDto payload) {
        return ResponseEntity.ok(service.getStudentClass(payload));
    }
}
