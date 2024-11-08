package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Reminder;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {
}
