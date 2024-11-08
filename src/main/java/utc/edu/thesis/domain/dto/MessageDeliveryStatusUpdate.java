package utc.edu.thesis.domain.dto;

import lombok.*;
import utc.edu.thesis.common.MessageDeliveryStatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDeliveryStatusUpdate {

    private Long id; // The unique identifier of the message or conversation entity
    private MessageDeliveryStatusEnum messageDeliveryStatusEnum; // The current delivery status of the message
    private String content; // The content of the message
    private Long timestamp;
}
