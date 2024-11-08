package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Student;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("select e from Student e where e.code = ?1")
    Optional<Student> findByCode(String code);

    @Query("select s from Student s where s.id = :id")
    Optional<Student> findById(@Param("id") Long id);

    @Query("select count(s) > 0 from Student s where s.code = :code")
    boolean existsByCode(@Param("code") String code);
}
