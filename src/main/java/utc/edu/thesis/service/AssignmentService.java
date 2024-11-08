package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.AssignmentDto;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.TeacherDto;
import utc.edu.thesis.domain.entity.Assignment;

import java.util.List;

@Service
public interface AssignmentService {
    AssignmentDto addAssign(AssignmentDto request);
    Boolean deleteAssign(AssignmentDto assignmentDto);
    List<AssignmentDto> getAssign(SearchDto dto);
    List<AssignmentDto> getAssignStudent(SearchDto dto);
    Integer countAssignmentBySession(Long sessionId);
    List<AssignmentDto> getStudent(Long sessionId, Long teacherId);

    Boolean changeStatus(AssignmentDto payload);
}
