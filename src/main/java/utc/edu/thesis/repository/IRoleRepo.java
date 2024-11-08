package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Role;

@Repository
public interface IRoleRepo extends JpaRepository<Role, Long> {
    Role findByName(String roleName);

}
