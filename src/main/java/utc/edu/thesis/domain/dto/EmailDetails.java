package utc.edu.thesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailDetails {
    // Class data members
    private String[] recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
