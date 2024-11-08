package utc.edu.thesis.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserConnection {
    private Long connectionId;
    private String username;
    private String status;
}
