package utc.edu.thesis.domain.dto;

import lombok.*;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.domain.entity.User;
import utc.edu.thesis.util.Constants;

import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String fullName;
    private Set<Role> roles;
    private boolean online;

    public static User toEntity(UserResponse dto) {
        return Constants.map().convertValue(dto, User.class);
    }

    public static UserResponse toDto(User entity) {
        return Constants.map().convertValue(entity, UserResponse.class);
    }
}
