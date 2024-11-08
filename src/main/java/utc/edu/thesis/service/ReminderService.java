package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.ReminderDto;
import utc.edu.thesis.domain.dto.SearchDto;

import java.util.List;

@Service
public interface ReminderService {
    List<ReminderDto> getReminder(SearchDto searchDto);
    ReminderDto addReminder(ReminderDto dto);
    ReminderDto editReminder(ReminderDto dto);
    Boolean deleteReminder(Long id);
    void schedulerCacheDay(ReminderDto dto);
    void schedulerCacheHour(ReminderDto dto);
}
