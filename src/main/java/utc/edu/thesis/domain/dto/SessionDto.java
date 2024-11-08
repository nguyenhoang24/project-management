package utc.edu.thesis.domain.dto;

import lombok.Data;
import utc.edu.thesis.domain.entity.Session;
import utc.edu.thesis.util.ObjectMapperUtil;

@Data
public class SessionDto {
    private Long id;
    private Integer year;
    private String notes;
    private String createdBy;
    private Integer amount;
    private Boolean status;

    public static SessionDto of(Session entity) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(entity, SessionDto.class);
    }

    public static Session toEntity(SessionDto dto) {
        return ObjectMapperUtil.OBJECT_MAPPER.convertValue(dto, Session.class);
    }
}
