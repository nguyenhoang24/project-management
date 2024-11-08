package utc.edu.thesis.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.domain.entity.User;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean status;
    private Set<Role> roles;

    public static UserDto of(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .roles(user.getRoles())
                .status(user.isEnabled())
                .build();
    }

    public static final UserDto of(TeacherDto dto) {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(3L,"ROLE_TEACHER"));
        return UserDto.builder()
                .username(dto.getPhone())
                .password("123")
                .email(dto.getEmail())
                .roles(roles)
                .status(true)
                .build();
    }

    public static final UserDto of(StudentDto dto) {
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(2L,"ROLE_STUDENT"));
        return UserDto.builder()
                .username(dto.getCode())
                .password("123")
                .email(dto.getEmail())
                .roles(roles)
                .status(true)
                .build();
    }
}
