package utc.edu.thesis.controller.admin;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.common.ExcelCM;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.Assignment;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.StudentClass;
import utc.edu.thesis.domain.entity.Teacher;
import utc.edu.thesis.repository.AssignmentRepository;
import utc.edu.thesis.service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/assignment")
@RequiredArgsConstructor
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final SessionService sessionService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AssignmentRepository assignmentRepository;
    private final StudentClassService classService;

    @PostMapping("/get-assignment")
    public ResponseEntity<List<AssignmentDto>> getBySession(@RequestBody SearchDto dto) {
        List<AssignmentDto> assignments = assignmentService.getAssign(dto);
        return ResponseEntity.ok(assignments);
    }

    @PostMapping("/get-student/{sessionId}/{teacherId}")
    public ResponseEntity<List<AssignmentDto>> getBySession(@PathVariable Long sessionId, @PathVariable Long teacherId) {
        return ResponseEntity.ok(assignmentService.getStudent(sessionId, teacherId));
    }

    @PostMapping(value = "/import-assignment", consumes = {"multipart/form-data"})
    public ResponseEntity<List<AssignmentDto>> addStudent(@RequestParam("session") Long sessionId, @RequestParam("fileExcel") MultipartFile fileExcel) {
        List<AssignmentDto> res = new ArrayList<>();
        try {
            JSONArray dataExcel = ExcelCM.readExcelAll(fileExcel);
            for (Object dataOne : dataExcel) {
                JSONArray dataOneArr = (JSONArray) dataOne;

                StudentDto studentDto = studentService.findByCode(dataOneArr.getString(2));
                TeacherDto teacherDto = teacherService.getTeacher(
                        new SearchDto(dataOneArr.getString(6), "EMAIL")
                ).stream().findFirst().orElseThrow();

                Student student = StudentDto.toEntity(studentDto);
                Teacher teacher = TeacherDto.toEntity(teacherDto);

                var assignmentS = assignmentRepository.findByStudentId(student.getId());
                if (assignmentS.isPresent()) {
                    continue;
                }

                Assignment assignment = Assignment.builder()
                        .session(sessionService.getSession(new SearchDto(String.valueOf(sessionId), "ID"))
                                .stream().findFirst().map(SessionDto::toEntity).get())
                        .student(student)
                        .teacher(teacher)
                        .createdBy("admin")
                        .createdDate(LocalDateTime.now())
                        .build();

                res.add(assignmentService.addAssign(AssignmentDto.of(assignment)));


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(res);
    }

    @PostMapping("/delete-assignment")
    public ResponseEntity<Boolean> deleteAssignment(@RequestBody AssignmentDto assignmentDto) {
        return ResponseEntity.ok(assignmentService.deleteAssign(assignmentDto));
    }

    @PostMapping("/add-assignment")
    public ResponseEntity<AssignmentDto> addAssignment(@RequestBody AssignmentDto assignmentDto) {
        return ResponseEntity.ok(assignmentService.addAssign(assignmentDto));
    }

    @PostMapping("/change-status")
    public ResponseEntity<Boolean> getSession(@RequestBody AssignmentDto payload) {
        return ResponseEntity.ok(assignmentService.changeStatus(payload));
    }
}
