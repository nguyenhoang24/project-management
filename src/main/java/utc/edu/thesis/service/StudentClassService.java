package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.entity.StudentClass;

import java.util.List;

@Service
public interface StudentClassService {
    StudentClass findByNameAndCourse(String name, Integer course);

    List<Integer> getAll();

    List<StudentClass> getStudentClass(SearchDto dto);


}
