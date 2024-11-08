package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.Role;
import org.springframework.security.core.userdetails.UserDetails;
import utc.edu.thesis.domain.entity.User;

import java.util.List;

@Service
public interface UserService {

    UserDto saveUser(UserDto userRequest);

    UserDto updateUser(UserDto userDto);

    Role saveRole(Role role);

    UserResponse getCurrentUser();

    User getUser(String username);

    UserDetails getUserByUsername(String username);

    void addRoleToUser(String username, String roleName);

    List<UserDto> getUser(SearchDto searchDto);

    Boolean deleteUser(Long id);

    List<Role> getRoles();

    Boolean changePassword(UserChangePassword request);

    User getUserById(Long id);
}
