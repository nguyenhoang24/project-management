package utc.edu.thesis.domain.dto;

import lombok.Data;
import utc.edu.thesis.domain.entity.Reminder;
import utc.edu.thesis.domain.entity.Session;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.Teacher;
import utc.edu.thesis.util.ObjectMapperUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReminderDto {
    private Long id;
    private String title;
    private String content;
    private String classNames;
    private LocalDateTime start;
    private LocalDateTime end;
    private TeacherDto teacher;
    private StudentDto student;
    private SessionDto session;
    private String recipient;

    public static ReminderDto of(Reminder entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, ReminderDto.class);
    }

    public static Reminder toEntity(ReminderDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Reminder.class);
    }

}
