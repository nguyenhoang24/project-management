package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.SessionDto;

import java.util.List;

@Service
public interface SessionService {
    SessionDto addSession(SessionDto request);

    SessionDto editSession(SessionDto request);

    Boolean deleteSession(Long id);

    List<SessionDto> getSession(SearchDto dto);

    Boolean changeStatus(SessionDto payload);

    SessionDto getSessionActive();
}
