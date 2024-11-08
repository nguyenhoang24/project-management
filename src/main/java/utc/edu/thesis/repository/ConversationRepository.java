package utc.edu.thesis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import utc.edu.thesis.domain.entity.Conversation;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {

    @Query("SELECT c FROM Conversation c JOIN c.members m WHERE m.user.id IN :userIds GROUP BY c.id HAVING COUNT(m.user.id) = :size")
    List<Conversation> findByTypeAndMembers(@Param("userIds") List<Long> userIds, @Param("size") int size);

    @Query("SELECT c FROM Conversation c JOIN c.members m WHERE m.user.id = :userId AND c.type = 'chat-ai'")
    Optional<Conversation> findByTypeAndUser(@Param("userId") Long userId);

    @Override
    Optional<Conversation> findById(Long aLong);

    @Query("SELECT c FROM Conversation c JOIN FETCH c.members WHERE c.id = :conversationId")
    Optional<Conversation> findByIdWithMembers(@Param("conversationId") Long conversationId);

}


