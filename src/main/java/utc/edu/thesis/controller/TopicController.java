package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.dto.TopicDto;
import utc.edu.thesis.service.TopicService;

import java.util.List;

@RestController
@RequestMapping("/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @PostMapping("/get-topic")
    public ResponseEntity<List<TopicDto>> getTopic(@RequestBody SearchDto searchDto) {
        return ResponseEntity.ok(topicService.getTopic(searchDto));
    }

    @PostMapping("/edit-topic")
    public ResponseEntity<TopicDto> editTopic(@RequestBody TopicDto searchDto) {
        return ResponseEntity.ok(topicService.editTopic(searchDto));
    }

    @PostMapping("/delete-topic/{id}")
    public ResponseEntity<Boolean> deleteTopic(@PathVariable Long id) {
        return ResponseEntity.ok(topicService.deleteTopic( id));
    }

    @PostMapping("/add-topic")
    public ResponseEntity<TopicDto> addTopic(@RequestBody TopicDto searchDto) {
        return ResponseEntity.ok(topicService.addTopic(searchDto));
    }
}
