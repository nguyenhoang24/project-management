package utc.edu.thesis.domain.entity;

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
public class JwtResponse {
    private Long id;

    private String token;

    private String type = "Bearer your.jwt.token";

    private String username;

    private Collection<? extends GrantedAuthority> roles;

    public JwtResponse(String accessToken, Long id, String username, Collection<? extends GrantedAuthority> roles) {
        this.token = accessToken;
        this.username = username;
        this.roles = roles;
        this.id = id;
    }
}
