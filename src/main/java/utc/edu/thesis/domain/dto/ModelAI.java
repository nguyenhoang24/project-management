package utc.edu.thesis.domain.dto;

import com.amazonaws.services.dynamodbv2.xspec.S;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ModelAI {

    private String model;

    private List<MessageAI> messages;

    private Boolean stream;

    private Float temperature;

}
