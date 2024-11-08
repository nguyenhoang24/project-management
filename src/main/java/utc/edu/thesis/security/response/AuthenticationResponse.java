package utc.edu.thesis.security.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private String username;
    private Long userId;
    private Collection<? extends GrantedAuthority> authorities;
    private int expired;
    private long studentId;
    private long teacherId;
    private long sessionId;

    public AuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
