package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.dto.AssignmentDto;
import utc.edu.thesis.domain.entity.Assignment;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("select count(distinct e.teacher.id) from Assignment e where e.session.id = ?1")
    Integer countAssignmentBySession(Long sessionId);

    @Query("select e from Assignment e where e.session.id = ?1")
    List<Assignment> getAssignmentBySession(Long sessionId);

    @Query("select e from Assignment e where e.teacher.id = ?1")
    List<Assignment> getAssignmentByTeacherId(Long sessionId);

    @Query("select e from Assignment e where e.session.id = ?1 and e.teacher.id=?2")
    List<Assignment> getTeacherBySession(Long sessionId, Long teacherId);

    @Query("select e from Assignment e where e.session.id = ?1 and e.teacher.id=?2")
    List<Assignment> countStudentByAssignment(Long sessionId, Long teacherId);

    @Query("select e from Assignment e where e.session.id = ?1 and e.student.code=?2")
    List<Assignment> getStudentBySession(Long sessionId, String studentCode);

    @Query("select e from Assignment e where e.teacher.id=?1")
    List<Assignment> getStudentByTeacher(Long teacherId);

    @Modifying
    @Query("delete from Assignment e where e.session.id = :#{#sessionId} and e.teacher.id= :#{#teacherId}")
    void deleteBySession(@Param("sessionId") Long sessionId, @Param("teacherId") Long teacherId);

    @Modifying
    @Query("delete from Assignment e where e.session.id = :#{#sessionId} and e.student.id= :#{#studentId}")
    void deleteByStudent(@Param("sessionId") Long sessionId, @Param("studentId") Long studentId);

    @Query("select a from Assignment a where a.teacher.id = :teacherId")
    Optional<List<Assignment>> findByTeacherId(@Param("teacherId") Long teacherId);

    @Query("select a from Assignment a where a.student.id = :studentId")
    Optional<Assignment> findByStudentId(@Param("studentId") Long studentId);

}
