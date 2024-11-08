package utc.edu.thesis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse  {
    private final String message;
    private final HttpStatus httpStatus;
    private ZonedDateTime timestamp;


}
