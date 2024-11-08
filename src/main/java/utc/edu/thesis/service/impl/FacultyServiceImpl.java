package utc.edu.thesis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.entity.Faculty;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.FacultyRepository;
import utc.edu.thesis.service.FacultyService;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;

    @Override
    public Faculty findByName(String name) {
        return facultyRepository.findByName(name).orElseThrow(() -> {
            throw new NotFoundException("not found faculty with name %s".formatted(name));
        });
    }
}
