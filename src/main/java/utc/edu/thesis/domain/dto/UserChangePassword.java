package utc.edu.thesis.domain.dto;

import lombok.Data;

@Data
public class UserChangePassword {
    private String oldPassword;
    private String newPassword;
}
