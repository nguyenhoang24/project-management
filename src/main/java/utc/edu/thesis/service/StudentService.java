package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.StudentDto;
import utc.edu.thesis.domain.entity.Student;

import java.util.List;

@Service
public interface StudentService {
    StudentDto addStudent(StudentDto dto, boolean checkImport);

    Boolean deleteStudent(Long id);

    StudentDto editStudent(StudentDto dto);

    List<StudentDto> getStudent(SearchDto dto);

    StudentDto findByCode(String code);

    boolean existByCode(String code);

    List<Student> getAll();
}
