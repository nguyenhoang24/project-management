package utc.edu.thesis.domain.dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
public class UserRequest {
    private String username;
    private String password;
    private String email;
    private String role;

    public UserRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
