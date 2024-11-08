package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.ProjectDto;
import utc.edu.thesis.domain.dto.ProjectSearchDto;
import utc.edu.thesis.domain.dto.SearchDto;

import java.util.List;

@Service
public interface ProjectService {
    List<ProjectDto> getProjects(SearchDto searchDto);
    ProjectDto addProject(ProjectDto projectDto);
    ProjectDto editProject(ProjectDto projectDto);
    ProjectDto addOutlineFile(Long projectId, MultipartFile file, String type, String fileName);
//    ProjectDto deleteFile(Long projectId, MultipartFile file, String type);
    Boolean deleteProject(Long projectId);
    Boolean deleteFileReport(Long projectId);
    Boolean deleteFileOutline(Long projectId);
    List<ProjectDto> getProjectsFilter(ProjectSearchDto searchDto);

}
