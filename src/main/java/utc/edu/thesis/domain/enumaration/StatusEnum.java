package utc.edu.thesis.domain.enumaration;

import lombok.Getter;

public enum StatusEnum {
    INACTIVE(0),
    ACTIVE(1),
    RESERVE(2);

    @Getter
    private final int status;

    StatusEnum(int status) {
        this.status = status;
    }
}
