package utc.edu.thesis.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utc.edu.thesis.domain.dto.ReminderDto;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.service.ReminderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {
    private final ReminderService reminderService;

    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void remindEventAfterOneDay() {
        List<ReminderDto> reminders = reminderService.getReminder(new SearchDto("", ""));
        reminders.forEach(reminder -> {
            if(reminder.getStart().minusDays(1).toLocalDate().equals(LocalDate.now())) {
                reminderService.schedulerCacheDay(reminder);
            }
        });
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 5000)
    public void remindEventAfterTwoHours() {
        List<ReminderDto> reminders = reminderService.getReminder(new SearchDto("", ""));
        reminders.forEach(reminder -> {
            if(reminder.getStart().minusHours(2).equals(LocalDateTime.now())) {
                reminderService.schedulerCacheHour(reminder);
            }
        });
    }
}
