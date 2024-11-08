package utc.edu.thesis.domain.dto;

import lombok.Data;
import utc.edu.thesis.domain.entity.Assignment;
import utc.edu.thesis.util.ObjectMapperUtil;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class AssignmentDto {
    private Long id;
    private LocalDateTime createdDate;
    private String createdBy;
    private SessionDto session;
    private StudentDto student;
    private TeacherDto teacher;
    private Integer amount;
    private Boolean status;

    public static AssignmentDto of(Assignment entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, AssignmentDto.class);
    }

    public static Assignment toEntity(AssignmentDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Assignment.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssignmentDto that = (AssignmentDto) o;
        return Objects.equals(teacher, that.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teacher);
    }
}
