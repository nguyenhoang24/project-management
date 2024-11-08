package utc.edu.thesis.domain.dto;

import lombok.Builder;
import lombok.Data;
import utc.edu.thesis.domain.entity.Teacher;
import utc.edu.thesis.util.ObjectMapperUtil;

import java.time.LocalDate;

@Data
@Builder
public class TeacherDto {
    private Long id;
    private String fullName;
    private LocalDate dob;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private String regulation;
    private String text;

    public static TeacherDto of(Teacher entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, TeacherDto.class);
    }

    public static Teacher toEntity(TeacherDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Teacher.class);
    }
}
