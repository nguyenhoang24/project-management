package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query("select e from Teacher e")
    List<Teacher> getAll();

    @Query("select t from Teacher t where t.id = :id")
    Optional<Teacher> findById(@Param("id") Long id);
}
