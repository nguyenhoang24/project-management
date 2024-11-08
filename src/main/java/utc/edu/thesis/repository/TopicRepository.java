package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Topic;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

}
