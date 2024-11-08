package utc.edu.thesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {

    private String token;

    private String type = "Bearer";

    private Long userId;

    private String username;

    private String fullName;

//    private String lastName;

    private String email;

//    private String phoneNumber;

    private List<String> roleList;

    private String refreshToken;

    public JwtResponse(String token, Long userId, String username, String email, List<String> roleList, String refreshToken) {
        this.token = token;
        this.userId = userId;
        this.username = username;
//        this.fullName = firstName;
//        this.lastName = lastName;
        this.email = email;
//        this.phoneNumber = phoneNumber;
        this.roleList = roleList;
        this.refreshToken = refreshToken;
    }
}
