package utc.edu.thesis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.entity.Role;
import utc.edu.thesis.repository.IRoleRepo;
import utc.edu.thesis.service.IRoleService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final IRoleRepo iRoleRepo;

    @Override
    public Role findByName(String name) {
        return iRoleRepo.findByName(name);
    }

    @Override
    public List<Role> getRoles() {
        return iRoleRepo.findAll();
    }

    @Override
    public Role save(Role role) {
        if (role != null && findByName(role.getName()) == null) {
            return iRoleRepo.save(role);
        }
        return null;
    }

    @Override
    public Iterable<Role> findAll() {
        return iRoleRepo.findAll();
    }

    @Override
    public Optional<Role> findById(Long id) {
        return iRoleRepo.findById(id);
    }

    @Override
    public Boolean delete(Long id) {
        if (id != null) {
            iRoleRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
