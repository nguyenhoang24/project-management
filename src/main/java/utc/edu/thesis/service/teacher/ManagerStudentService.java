package utc.edu.thesis.service.teacher;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.StudentDto;

import java.util.List;

@Service
public interface ManagerStudentService {
    List<StudentDto> getStudent(SearchDto searchDto);
}
