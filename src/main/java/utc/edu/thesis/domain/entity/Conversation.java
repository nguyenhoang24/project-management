package utc.edu.thesis.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type; // '1-1' or 'group'

    @Column(name = "created_at")
    private Long createdAt; // Timestamp of when the conversation was created

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER,orphanRemoval = true)
    @JsonManagedReference
    private Set<ConversationMember> members = new HashSet<>();

}
