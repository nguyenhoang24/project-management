package utc.edu.thesis.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.StudentDto;
import utc.edu.thesis.domain.dto.UserDto;
import utc.edu.thesis.domain.entity.Student;
import utc.edu.thesis.exception.request.BadRequestException;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.StudentRepository;
import utc.edu.thesis.service.StudentService;
import utc.edu.thesis.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final EntityManager entityManager;
    private final UserService userService;

    public StudentDto getById(Long id) {
        return studentRepository.findById(id)
                .map(StudentDto::of)
                .orElseThrow(() -> {
                    throw new BadRequestException("Can't not find teacher by Id: %d".formatted(id));
                });
    }

    @Override
    public StudentDto addStudent(StudentDto dto, boolean checkImport) {
        if (dto != null) {
            if (studentRepository.findByCode(dto.getCode()).isPresent()) {
                throw new BadRequestException("Student with code: %s existed".formatted(dto.getCode()));
            }

            Student student = Student.builder()
                    .code(dto.getCode())
                    .fullName(dto.getFullName())
                    .dob(dto.getDob())
                    .gender(dto.getGender())
                    .email(dto.getEmail())
                    .address(dto.getAddress())
                    .phone(dto.getPhone())
                    .studentClass(dto.getStudentClass())
                    .build();

            if (!checkImport) {
                UserDto userDto = UserDto.of(dto);
                userService.saveUser(userDto);
            }

            studentRepository.save(student);

            return StudentDto.of(student);
        }
        return null;
    }

    @Override
    public Boolean deleteStudent(Long id) {
        if (id != null) {
            StudentDto res = getById(id);
            if (res != null) {
                studentRepository.delete(StudentDto.toEntity(res));
                return true;
            }
        }
        return false;
    }

    @Override
    public StudentDto editStudent(StudentDto dto) {

        if (dto.getCode() == null) {

            throw new BadRequestException("Code is null");

        }
        StudentDto response = findByCode(dto.getCode());

        Student entity = Student.builder()
                .id(response.getId())
                .code(dto.getCode())
                .fullName(dto.getFullName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                .dob(dto.getDob())
                .email(dto.getEmail())
                .address(dto.getAddress())
                .studentClass(dto.getStudentClass())
                .build();

        return StudentDto.of(studentRepository.save(entity));
    }

    @Override
    public List<StudentDto> getStudent(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Student as e where(1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {

            if ("NAME".equals(dto.getConditionSearch())) {
                whereClause += " AND e.fullName like '%" + dto.getValueSearch() + "%'";
            }
            if ("EMAIL".equals(dto.getConditionSearch())) {
                whereClause += " AND e.email like '%" + dto.getValueSearch() + "%'";
            }
            if ("PHONE".equals(dto.getConditionSearch())) {
                whereClause += " AND e.phone like '%" + dto.getValueSearch() + "%'";
            }
            if ("CODE".equals(dto.getConditionSearch())) {
                whereClause += " AND e.code = '" + dto.getValueSearch() + "'";
            }
            if ("CLASS".equals(dto.getConditionSearch())) {
                whereClause += " AND e.studentClass.id like '%" + dto.getValueSearch() + "%'";
            }
            if ("ID".equals(dto.getConditionSearch())) {
                whereClause += " AND e.id = " + dto.getValueSearch();
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Student.class);
        List<Student> resultQuery = q.getResultList();

        return resultQuery.stream().map(StudentDto::of).collect(Collectors.toList());
    }

    @Override
    public StudentDto findByCode(String code) {
        return studentRepository.findByCode(code)
                .map(StudentDto::of).orElseThrow(() -> {
                    throw new NotFoundException("not found student with code: %s".formatted(code));
                });
    }

    @Override
    public boolean existByCode(String code) {
        return studentRepository.existsByCode(code);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }
}
