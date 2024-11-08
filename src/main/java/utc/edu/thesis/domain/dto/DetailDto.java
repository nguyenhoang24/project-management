package utc.edu.thesis.domain.dto;

import lombok.Data;
import utc.edu.thesis.domain.entity.Detail;
import utc.edu.thesis.domain.entity.Project;
import utc.edu.thesis.util.ObjectMapperUtil;

import java.time.LocalDate;

@Data
public class DetailDto {
    private Long id;
    private String title;
    private Integer status;
    private String comment;
    private String reportFile;
    private LocalDate startDate;
    private LocalDate endDate;
    private Project project;

    public static DetailDto of(Detail entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, DetailDto.class);
    }

    public static Detail toEntity(DetailDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Detail.class);
    }
}
