package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.TeacherDto;
import utc.edu.thesis.domain.entity.Teacher;

import java.util.List;

@Service
public interface TeacherService {
    List<Teacher> getAll();

    TeacherDto getById(Long id);

    TeacherDto addTeacher(TeacherDto dto);

    Boolean deleteTeacher(Long id);

    TeacherDto editTeacher(TeacherDto dto);

    List<TeacherDto> getTeacher(SearchDto dto);

    String addRegulation(TeacherDto dto);
}
