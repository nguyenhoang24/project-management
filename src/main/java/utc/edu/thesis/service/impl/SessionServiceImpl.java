package utc.edu.thesis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utc.edu.thesis.domain.dto.AssignmentDto;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.SessionDto;
import utc.edu.thesis.domain.entity.Session;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.SessionRepository;
import utc.edu.thesis.service.AssignmentService;
import utc.edu.thesis.service.SessionService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final AssignmentService assignmentService;
    private final EntityManager entityManager;

    @Override
    public SessionDto addSession(SessionDto request) {
        Session session = Session.builder()
                .year(request.getYear())
                .createdBy("admin")
                .build();

        return SessionDto.of(sessionRepository.save(session));
    }

    @Override
    public SessionDto editSession(SessionDto request) {
        if (request.getId() != null) {
            Session response = sessionRepository.findById(request.getId()).orElseThrow(() -> {
                throw new NotFoundException("not found id: %d".formatted(request.getId()));
            });

            Session session = Session.builder()
                    .id(response.getId())
                    .year(request.getYear())
                    .build();

            return SessionDto.of(sessionRepository.save(session));
        }
        return null;
    }

    @Override
    public Boolean deleteSession(Long id) {
        if (id != null) {
            Session res = sessionRepository.findById(id).orElseThrow(() -> {
                throw new NotFoundException("not found id: %d".formatted(id));
            });

            if (res != null && assignmentService.countAssignmentBySession(res.getId()) == 0) {
                sessionRepository.delete(res);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<SessionDto> getSession(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Session as e where (1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {
            if ("YEAR".equals(dto.getConditionSearch())) {
                whereClause += " AND e.year = " + dto.getValueSearch();
            }
            if("STATUS".equals(dto.getConditionSearch())) {
                whereClause += " AND e.status = true";
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Session.class);
        List<Session> sessions = q.getResultList();
        List<SessionDto> resultList = new ArrayList<>();
        sessions.forEach(res -> {
            SessionDto sessionDto = SessionDto.of(res);
            sessionDto.setAmount(assignmentService.countAssignmentBySession(res.getId()));
            resultList.add(sessionDto);
        });

        return resultList;
    }

    @Override
    public Boolean changeStatus(SessionDto payload) {
        if (payload != null) {
            Session session = sessionRepository.findById(payload.getId()).orElseThrow();

            session.setStatus(payload.getStatus());
            sessionRepository.save(session);
            AssignmentDto assignmentDto = new AssignmentDto();
            assignmentDto.setSession(SessionDto.of(session) );
            assignmentDto.setStatus(payload.getStatus());
            assignmentService.changeStatus(assignmentDto);
            return true;
        }
        return false;
    }

    @Override
    public SessionDto getSessionActive() {
        return SessionDto.of(sessionRepository.getSessionActive());
    }
}
