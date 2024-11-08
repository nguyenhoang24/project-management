package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.domain.entity.StudentClass;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentClassRepository extends JpaRepository<StudentClass, Long> {
    @Query("select e from StudentClass e where e.name = ?1 and e.course = ?2")
    Optional<StudentClass> findByNameAndCourse(String name, Integer course);

    @Query("select distinct e.course from StudentClass e")
    List<Integer> selectCourse();
}
