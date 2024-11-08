package utc.edu.thesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utc.edu.thesis.domain.entity.Project;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.Teacher;
import utc.edu.thesis.domain.entity.Topic;
import utc.edu.thesis.domain.enumaration.StatusEnum;
import utc.edu.thesis.util.ObjectMapperUtil;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private Long id;
    private String name;
    private LocalDateTime createDate;
    private LocalDateTime endDate;
    private StudentDto student;
    private TeacherDto teacher;
    private TopicDto topic;
    private String reportFile;
    private String outlineFile;
    private SessionDto session;
    private StatusEnum status;

    public static ProjectDto of(Project entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, ProjectDto.class);
    }

    public static Project toEntity(ProjectDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Project.class);
    }
}
