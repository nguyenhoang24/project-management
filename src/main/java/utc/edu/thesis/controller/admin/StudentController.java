package utc.edu.thesis.controller.admin;

import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.common.ExcelCM;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.StudentDto;
import utc.edu.thesis.domain.dto.UserDto;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.StudentClass;
import utc.edu.thesis.exception.request.BadRequestException;
import utc.edu.thesis.service.FacultyService;
import utc.edu.thesis.service.StudentClassService;
import utc.edu.thesis.service.StudentService;
import utc.edu.thesis.service.UserService;
import utc.edu.thesis.util.DateUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final FacultyService facultyService;
    private final StudentClassService classService;
    private final UserService userService;

    @PostMapping("/add-student")
    public ResponseEntity<StudentDto> addStudent(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.addStudent(dto, false));
    }

    @PostMapping("/delete-student/{id}")
    public ResponseEntity<Boolean> deleteStudent(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.deleteStudent(id));
    }

    @PostMapping("/edit-student")
    public ResponseEntity<StudentDto> editStudent(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.editStudent(dto));
    }

    @PostMapping("/get-student")
    public ResponseEntity<List<StudentDto>> getStudent(@RequestBody SearchDto payload) {
        return ResponseEntity.ok(studentService.getStudent(payload));
    }

    @PostMapping(value = "/import-student", consumes = {"multipart/form-data"})
    public ResponseEntity<List<StudentDto>> addStudent(@RequestParam("fileExcel") MultipartFile fileExcel) {
        List<StudentDto> res = new ArrayList<>();
        try {
            JSONArray dataExcel = ExcelCM.readExcelAll(fileExcel);
            for (Object dataOne : dataExcel) {
                JSONArray dataOneArr = (JSONArray) dataOne;

                // Extract class and course from the input (e.g., "72DCTT23")
                String classStr = dataOneArr.getString(2);
                String courseStr = classStr.substring(0, 2); // Extract "72" as the course number
                Integer course = Integer.valueOf(courseStr);
                String className = classStr.substring(0); // Extract the class name (e.g., "DCTT23")

                // Find or create the student class based on the course and class name
                StudentClass studentClass = classService.findByNameAndCourse(className, course);

                // Parse gender value, converting it from float to int (if applicable)
                String genderStr = dataOneArr.getString(3);
                Integer gender = (int) Double.parseDouble(genderStr);

                // Xử lý ngày tháng năm sinh (định dạng 'yyyy-MM-dd')
                String dobStr = dataOneArr.getString(5);
                LocalDate dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                // Create the student object with the input data
                Student student = Student.builder()
                        .code(dataOneArr.getString(0)) // Code
                        .fullName(dataOneArr.getString(1)) // Full Name
                        .studentClass(studentClass) // Class
                        .gender(gender) // Gender (converted from float to integer)
                        .phone(dataOneArr.getString(4)) // Phone
                        .dob(dob) // DOB (Date of Birth)
                        .email(dataOneArr.getString(6)) // Email
                        .address(dataOneArr.getString(7)) // Address
                        .build();

                try {
                    if (studentService.existByCode(student.getCode())) {
                        continue;
                    }
                    // Create a user DTO from the student and save the user
                    UserDto userDto = UserDto.of(StudentDto.of(student));
                    userService.saveUser(userDto);

                    // Add the student and save it
                    res.add(studentService.addStudent(StudentDto.of(student), true));
                } catch (BadRequestException e) {
                    // If the student already exists, update their information
                    res.add(studentService.editStudent(StudentDto.of(student)));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel file", e);
        }
        return ResponseEntity.ok(res);
    }


}
