package utc.edu.thesis.domain.dto;

import lombok.*;

import java.util.List;

/**
 * Represents a chat message in the chat application.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private Long id;
    private String content;
    private String sender;
    private Long userId;
    private MessageType type;
    private UserConnection userConnection;
    private Long conversationId;
    private List<MessageDeliveryStatusUpdate> messageDeliveryStatusUpdates;
    private String chatType;

    /**
     * Enum representing the type of the chat message.
     */
    public enum MessageType {
        CHAT, LEAVE, JOIN, FRIEND_ONLINE, FRIEND_OFFLINE, MESSAGE_DELIVERY_UPDATE
    }

    /**
     * Gets the content of the chat message.
     *
     * return the content of the chat message
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the chat message.
     *
     * param content the content of the chat message
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets the sender of the chat message.
     *
     * return the sender of the chat message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender of the chat message.
     *
     * param sender the sender of the chat message
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Gets the type of the chat message.
     *
     * return the type of the chat message
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Sets the type of the chat message.
     *
     * param type the type of the chat message
     */
    public void setType(MessageType type) {
        this.type = type;
    }


    @Override
    public String toString() {
        return "ChatMessage{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", sender='" + sender + '\'' +
                ", type=" + type +
                ", userConnection=" + userConnection +
                ", conversationId=" + conversationId +
                ", messageDeliveryStatusUpdates=" + messageDeliveryStatusUpdates +
                '}';
    }
}
