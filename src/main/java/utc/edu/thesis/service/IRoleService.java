package utc.edu.thesis.service;

import utc.edu.thesis.domain.entity.Role;

import java.util.List;

public interface IRoleService extends IService<Role> {
    Role findByName(String name);
    List<Role> getRoles();
}
