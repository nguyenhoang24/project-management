package utc.edu.thesis.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.Project;
import utc.edu.thesis.domain.enumaration.StatusEnum;
import utc.edu.thesis.exception.request.BadRequestException;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.ProjectRepository;
import utc.edu.thesis.service.AmazonUploadService;
import utc.edu.thesis.service.ProjectService;
import utc.edu.thesis.service.SessionService;
import utc.edu.thesis.service.StudentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final AmazonUploadService aws3Service;
    private final SessionService sessionService;
    private final EntityManager entityManager;
    private final StudentService studentService;

    private final String pathUrl;

    public ProjectServiceImpl(ProjectRepository projectRepository,
                              AmazonUploadService aws3Service,
                              SessionService sessionService,
                              EntityManager entityManager,
                              StudentService studentService,
                              @Value("${upload.url}") String pathUrl) {
        this.projectRepository = projectRepository;
        this.aws3Service = aws3Service;
        this.sessionService = sessionService;
        this.entityManager = entityManager;
        this.studentService = studentService;
        this.pathUrl = pathUrl;
    }

    @Override
    public List<ProjectDto> getProjects(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Project as e where(1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {
            if ("NAME".equals(dto.getConditionSearch())) {
                whereClause += "AND e.name like '%" + dto.getValueSearch() + "%'";
            }
            if ("STUDENT".equals(dto.getConditionSearch())) {
                whereClause += "AND e.student.id = " + dto.getValueSearch()
                        + " AND e.session.id = " + sessionService.getSessionActive().getId();
            }
            if ("ID".equals(dto.getConditionSearch())) {
                whereClause += "AND e.id = " + dto.getValueSearch();
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Project.class);
        List<Project> resQuery = q.getResultList();
        List<ProjectDto> res = new ArrayList<>();

        resQuery.forEach(project -> {
            ProjectDto projectDto = ProjectDto.of(project);
            projectDto.setStatus(StatusEnum.values()[project.getStatus()]);
            res.add(projectDto);
        });
        return res;
    }

    @Override
    public ProjectDto addProject(ProjectDto dto) {
        if (dto != null) {
            Project project = new Project();
            List<ProjectDto> projectDtos = getProjects(new SearchDto(String.valueOf(dto.getStudent().getId()), "STUDENT"));
            if (!projectDtos.isEmpty()) {
                throw new BadRequestException("Student had a project");
            }

            if (projectRepository.projectExistInYear(dto.getName().trim(), LocalDate.now().getYear())) {
                throw new IllegalArgumentException("Project exist");
            }

            if (dto.getStudent().getCode() != null) {
                dto.setStudent(studentService.findByCode(dto.getStudent().getCode()));
                project = projectRepository.findByStudentCode(dto.getStudent().getCode());
            }
            if (project != null && project.getName() != null) {
                throw new BadRequestException("Student had project");
            }
            project = Project.builder()
                    .createDate(LocalDateTime.now())
                    .name(dto.getName())
                    .outlineFile(dto.getOutlineFile())
                    .reportFile(dto.getReportFile())
                    .topic(TopicDto.toEntity(dto.getTopic()))
                    .session(SessionDto.toEntity(dto.getSession()))
                    .teacher(TeacherDto.toEntity(dto.getTeacher()))
                    .student(StudentDto.toEntity(dto.getStudent()))
                    .status(1)
                    .build();

            projectRepository.save(project);

            return ProjectDto.of(project);
        }
        return null;
    }

    @Override
    public ProjectDto editProject(ProjectDto dto) {
        if (dto.getId() == null) {
            throw new BadRequestException("Id is null");

        }
        Project project = Project.builder()
                .id(dto.getId())
                .createDate(LocalDateTime.now())
                .name(dto.getName())
                .outlineFile(dto.getOutlineFile())
                .reportFile(dto.getReportFile())
                .topic(TopicDto.toEntity(dto.getTopic()))
                .session(SessionDto.toEntity(dto.getSession()))
                .teacher(TeacherDto.toEntity(dto.getTeacher()))
                .student(StudentDto.toEntity(dto.getStudent()))
                .status(1)
                .build();

        projectRepository.save(project);
        return ProjectDto.of(project);
    }

    @Override
    public ProjectDto addOutlineFile(Long projectId, MultipartFile file, String type, String fileName) {
        if (!file.isEmpty()) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not find project with id: %lf".formatted(projectId));
                    });

//            String uploadFile = aws3Service.uploadFile(file);
            if ("OUTLINE".equals(type)) {
                project.setOutlineFile(pathUrl + "/api/files?filePath=uploads/" + fileName);
            } else if ("REPORT".equals(type)) {
                project.setReportFile(pathUrl + "/api/files?filePath=uploads/" + fileName);
            }
            projectRepository.save(project);
            return ProjectDto.of(project);
        }
        return null;
    }

    @Override
    public Boolean deleteProject(Long id) {
        if (id != null) {
            Project res = projectRepository.findById(id).orElseThrow(
                    () -> {
                        throw new NotFoundException("Not found project with id: %d".formatted(id));
                    }
            );
            projectRepository.delete(res);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteFileReport(Long projectId) {
        if (projectId != null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not find project with id: %d".formatted(projectId));
                    });

            project.setReportFile(null);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    @Override
    public Boolean deleteFileOutline(Long projectId) {
        if (projectId != null) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not find project with id: %d".formatted(projectId));
                    });

            project.setOutlineFile(null);
            projectRepository.save(project);
            return true;
        }
        return false;
    }

    @Override
    public List<ProjectDto> getProjectsFilter(ProjectSearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Project as e where(1=1) ";

        if (StringUtils.hasText(dto.getProjectName())) {
            whereClause += " AND UPPER(e.name) like UPPER('%" + dto.getProjectName() + "%')";
        }
        if (StringUtils.hasText(dto.getStudentName())) {
            whereClause += " AND UPPER(e.student.fullName) like UPPER('%" + dto.getStudentName() + "%')";
        }
        if (StringUtils.hasText(dto.getStudentCode())) {
            whereClause += " AND UPPER(e.student.code) like UPPER('%" + dto.getStudentCode() + "%')";
        }
        if (!dto.getCourse().isEmpty()) {
            whereClause += " AND e.student.studentClass.course in (" + org.apache.tomcat.util.buf.StringUtils.join(dto.getCourse(), ',') + ")";
        }
        if (!dto.getStudentClass().isEmpty()) {
            whereClause += " AND e.student.studentClass.id in (" + org.apache.tomcat.util.buf.StringUtils.join(dto.getStudentClass(), ',') + ")";
        }
        if (!dto.getSession().isEmpty()) {
            whereClause += " AND e.session.id in (" + org.apache.tomcat.util.buf.StringUtils.join(dto.getSession(), ',') + ")";
        }
        if (!dto.getTopic().isEmpty()) {
            whereClause += " AND e.topic.id in (" + org.apache.tomcat.util.buf.StringUtils.join(dto.getTopic(), ',') + ")";
        }
        if (!dto.getStatus().isEmpty()) {
            whereClause += " AND e.status in (" + org.apache.tomcat.util.buf.StringUtils.join(dto.getStatus(), ',') + ")";
        }

        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Project.class);
        List<Project> resQuery = q.getResultList();
        List<ProjectDto> res = new ArrayList<>();

        resQuery.forEach(project -> {
            ProjectDto projectDto = ProjectDto.of(project);
            projectDto.setStatus(StatusEnum.values()[project.getStatus()]);
            res.add(projectDto);
        });
        return res;
    }
}
