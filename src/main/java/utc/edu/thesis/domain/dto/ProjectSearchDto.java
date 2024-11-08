package utc.edu.thesis.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectSearchDto {
    private String projectName;
    private String studentCode;
    private String studentName;
    private List<String> topic;
    private List<String> studentClass;
    private List<String> session;
    private List<String> course;
    private List<String> status;
}
