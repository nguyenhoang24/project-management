package utc.edu.thesis.domain.dto;

import lombok.Data;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.StudentClass;
import utc.edu.thesis.util.ObjectMapperUtil;

import java.time.LocalDate;

@Data
public class StudentDto {
    private Long id;
    private String code;
    private String fullName;
    private LocalDate dob;
    private Integer gender;
    private String address;
    private String phone;
    private String email;
    private StudentClass studentClass;

    public static StudentDto of(Student entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, StudentDto.class);
    }

    public static Student toEntity(StudentDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Student.class);
    }
}
