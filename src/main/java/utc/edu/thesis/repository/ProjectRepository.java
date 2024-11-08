package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Project;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("select e from Project e where e.student.code = ?1")
    Project findByStudentCode(String code);

    @Query("select count(p) > 0 from Project p where p.name = :name and p.session.year = :year")
    boolean projectExistInYear(@Param("name") String name,
                               @Param("year") int year);
}
