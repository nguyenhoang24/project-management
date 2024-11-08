package utc.edu.thesis.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.Assignment;
import utc.edu.thesis.exception.request.BadRequestException;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.AssignmentRepository;
import utc.edu.thesis.service.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {
    private final AssignmentRepository assignmentRepository;
    private final UserService userService;
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final EntityManager entityManager;

    @Override
    public AssignmentDto addAssign(AssignmentDto request) {
        if (request != null) {
            if (request.getStudent().getId() == 0) {
                StudentDto studentDto = studentService.getStudent(
                                new SearchDto(request.getStudent().getCode(), "CODE"))
                        .stream().findFirst().orElseThrow(() -> {
                            throw new NotFoundException("Can't find student with code: %s".formatted(request.getStudent().getCode()));
                        });
                request.setStudent(studentDto);
            }

            if (!assignmentRepository.getStudentBySession(request.getSession().getId(), request.getStudent().getCode()).isEmpty()) {
                throw new BadRequestException("Student with code: %s existed".formatted(request.getStudent().getCode()));
            }

            Assignment assignment = Assignment.builder()
                    .createdDate(LocalDateTime.now())
                    .createdBy(userService.getCurrentUser().getUsername())
                    .session(SessionDto.toEntity(request.getSession()))
                    .teacher(TeacherDto.toEntity(request.getTeacher()))
                    .student(StudentDto.toEntity(request.getStudent()))
                    .status(false)
                    .build();

            assignmentRepository.save(assignment);

            return AssignmentDto.of(assignment);
        }
        return null;
    }

    @Transactional
    @Override
    public Boolean deleteAssign(AssignmentDto assignmentDto) {
        if (assignmentDto != null) {
            if (assignmentDto.getStudent() != null) {
                List<Assignment> assignments = assignmentRepository.countStudentByAssignment(assignmentDto.getSession().getId(), assignmentDto.getTeacher().getId());
                if (assignments.size() == 1) {
                    return false;
                }
                assignmentRepository.deleteByStudent(assignmentDto.getSession().getId(), assignmentDto.getStudent().getId());
                return true;
            }
            if (assignmentDto.getTeacher() != null && assignmentDto.getStudent() == null) {
                assignmentRepository.deleteBySession(assignmentDto.getSession().getId(), assignmentDto.getTeacher().getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AssignmentDto> getAssign(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Assignment as e where(1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {
            List<TeacherDto> teacher = teacherService.getTeacher(new SearchDto(dto.getValueSearch(), dto.getConditionSearch()));
            if ("EMAIL".equals(dto.getConditionSearch())) {
                whereClause += " AND e.teacher.id = " + teacher.get(0).getId();
            } else if ("SESSION".equals(dto.getConditionSearch())) {
                whereClause += " AND e.session.id = " + dto.getValueSearch();
            } else if ("ID".equals(dto.getConditionSearch())) {
                whereClause += " AND e.id = " + dto.getValueSearch();
            } else if ("TEACHER".equals(dto.getConditionSearch())) {
                whereClause += " AND e.student.id = " + dto.getValueSearch().split(",")[0]
                        + " AND e.session.id = " + dto.getValueSearch().split(",")[1];
            } else if ("STUDENT".equals(dto.getConditionSearch())) {
                whereClause += " AND e.teacher.id = " + dto.getValueSearch().split(",")[0]
                        + " AND e.session.id = " + dto.getValueSearch().split(",")[1];
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Assignment.class);
        List<Assignment> resultQuery = q.getResultList();
        List<AssignmentDto> res = new ArrayList<>();
        for (Assignment assignment : resultQuery) {
            AssignmentDto assignmentDto = AssignmentDto.of(assignment);
            assignmentDto.setAmount(
                    assignmentRepository.countStudentByAssignment(
                            assignment.getSession().getId(), assignment.getTeacher().getId()).size());
            res.add(assignmentDto);
        }

        return res.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<AssignmentDto> getAssignStudent(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Assignment as e where(1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {
            List<TeacherDto> teacher = teacherService.getTeacher(new SearchDto(dto.getValueSearch(), dto.getConditionSearch()));
           if ("STUDENT".equals(dto.getConditionSearch())) {
                whereClause += " AND e.teacher.id = " + dto.getValueSearch().split(",")[0]
                        + " AND e.session.id = " + dto.getValueSearch().split(",")[1];
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Assignment.class);
        List<Assignment> resultQuery = q.getResultList();
        List<AssignmentDto> res = new ArrayList<>();
        for (Assignment assignment : resultQuery) {
            AssignmentDto assignmentDto = AssignmentDto.of(assignment);
            assignmentDto.setAmount(
                    assignmentRepository.countStudentByAssignment(
                            assignment.getSession().getId(), assignment.getTeacher().getId()).size());
            res.add(assignmentDto);
        }

        return res;
    }

    @Override
    public List<AssignmentDto> getStudent(Long sessionId, Long teacherId) {
        if (sessionId != null && teacherId != null) {
            return assignmentRepository.countStudentByAssignment(sessionId, teacherId).stream()
                    .map(AssignmentDto::of)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Integer countAssignmentBySession(Long sessionId) {

        return assignmentRepository.countAssignmentBySession(sessionId);
    }

    @Override
    public Boolean changeStatus(AssignmentDto payload) {
        if (payload != null) {
//            if (payload.getSession() == null) {
//                Assignment assignment = assignmentRepository.findById(payload.getId()).orElseThrow();
//                assignment.setStatus(payload.getStatus());
//
//                assignmentRepository.save(assignment);
//                return true;
//            }
            List<Assignment> assignments = assignmentRepository.getAssignmentByTeacherId(payload.getId());

            if (!assignments.isEmpty()) {
                assignments.forEach(assignment -> {
                    assignment.setStatus(payload.getStatus());
                    assignmentRepository.save(assignment);
                });
                return true;
            }
        }
        return false;
    }
}
