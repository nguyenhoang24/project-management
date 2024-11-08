package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.entity.Faculty;

@Service
public interface FacultyService {
    Faculty findByName(String name);
}
