package utc.edu.thesis.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.UserChangePassword;
import utc.edu.thesis.domain.dto.UserDto;
import utc.edu.thesis.domain.dto.UserResponse;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.domain.entity.User;
import utc.edu.thesis.exception.request.BadRequestException;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.IRoleRepo;
import utc.edu.thesis.repository.IUserRepo;
import utc.edu.thesis.service.UserService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final IUserRepo userRepo;
    private final IRoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager entityManager;

    public UserDto getById(Long id) {
        return userRepo.findById(id)
                .map(UserDto::of)
                .orElseThrow(() -> {
                    throw new BadRequestException("UserId can not be found");
                });
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException("User not found in the database");
//        }
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        user.getRoles().forEach(
//                role -> authorities.add(new SimpleGrantedAuthority(role.getName()))
//        );
//
//        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
//    }

    @Override
    public UserDto saveUser(UserDto dto) {
        if (dto == null) {
            return null;
        }
        if (userRepo.findByUsername(dto.getUsername()) != null) {
            throw new NotFoundException("User existed");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(dto.getRoles())
                .enabled(true)
                .build();

        return UserDto.of(userRepo.save(user));
    }

    @Override
    public UserDto updateUser(UserDto dto) {
        if (dto == null) {
            throw new BadRequestException("data is null");
        }
        User response = userRepo.findById(dto.getId()).orElseThrow();

        User entity = User.builder()
                .id(response.getId())
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(dto.getRoles())
                .enabled(dto.getStatus())
                .build();

        return UserDto.of(userRepo.save(entity));
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public Boolean deleteUser(Long id) {
        if (id == null) {
            throw new BadRequestException("User Id is null");
        }
        // throw EntityNotfoundNotFound
        UserDto responseDto = getById(id);
        if (responseDto != null) {
            userRepo.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String userName;
        if (principal instanceof UserDetails) {
            UserDetails userDetail = (UserDetails) principal;
            userName = userDetail.getUsername();
        } else {
            userName = principal.toString();
        }

        if (userName != null) {
            return UserResponse.toDto(userRepo.findByUsername(userName));
        }

        return null;
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        UserResponse userDto = (UserResponse) getUserByUsername(username);
        User user = UserResponse.toEntity(userDto);

        Role role = roleRepo.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public List<UserDto> getUser(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from User as e where(1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {
            if ("USERNAME".equals(dto.getConditionSearch())) {
                whereClause += " AND e.username like '%" + dto.getValueSearch() + "%'";
            }
            if ("EMAIL".equals(dto.getConditionSearch())) {
                whereClause += " AND e.email like '%" + dto.getValueSearch() + "%'";
            }
            if ("ID".equals(dto.getConditionSearch())) {
                whereClause += " AND e.id = " + dto.getValueSearch();
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, User.class);
        List<User> resultQuery = q.getResultList();
        List<UserDto> res = new ArrayList<>();
        resultQuery.forEach(user -> res.add(UserDto.of(user)));
        return res;
    }

    @Override
    public UserDetails getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public User getUser(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public List<Role> getRoles() {
        return roleRepo.findAll();
    }

    @Override
    public Boolean changePassword(UserChangePassword request) {
        UserResponse currentUser = getCurrentUser();
        if (currentUser != null) {
            User user = userRepo.findById(currentUser.getId()).orElseThrow(
                    () -> {
                        throw new NotFoundException("Not found user with id: %d".formatted(currentUser.getId()));

                    }
            );
            if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                return false;
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepo.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User getUserById(Long id) {

        return userRepo.findById(id).orElse(null);
    }
}
