package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.TopicDto;
import utc.edu.thesis.domain.entity.StudentClass;

import java.util.List;

@Service
public interface TopicService {

    List<TopicDto> getTopic(SearchDto dto);
    TopicDto editTopic(TopicDto dto);
    TopicDto addTopic(TopicDto dto);
    Boolean deleteTopic(Long id);
}
