package utc.edu.thesis.service.teacher;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.ProjectDto;
import utc.edu.thesis.domain.dto.SearchDto;

import java.util.List;

@Service
public interface ManagerProjectService {
    List<ProjectDto> getProjects(SearchDto searchDto);
    ProjectDto addProject(ProjectDto projectDto);
}
