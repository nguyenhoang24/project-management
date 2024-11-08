package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.ConversationMember;

@Repository
public interface ConversationMemberRepository extends JpaRepository<ConversationMember, Long> {
}
