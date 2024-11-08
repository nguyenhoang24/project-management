package utc.edu.thesis.common;

public enum MessageDeliveryStatusEnum {
    SENT,          // Message has been sent
    DELIVERED,     // Message has been delivered to the recipient
    READ,          // Message has been read by the recipient
    FAILED,        // Message delivery failed
    PENDING        // Message is pending delivery
}
