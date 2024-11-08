package utc.edu.thesis.controller.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utc.edu.thesis.domain.dto.ProjectDto;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.service.teacher.ManagerProjectService;

import java.util.List;

@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class MangageProjectController {
    private final ManagerProjectService projectService;

    @PostMapping("/get-project")
    public ResponseEntity<List<ProjectDto>> getProjects(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(projectService.getProjects(searchDto));
    }
}
