package utc.edu.thesis.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utc.edu.thesis.domain.dto.*;
import utc.edu.thesis.domain.entity.Project;
import utc.edu.thesis.domain.entity.Topic;
import utc.edu.thesis.exception.request.BadRequestException;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.TopicRepository;
import utc.edu.thesis.service.TopicService;
import utc.edu.thesis.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final EntityManager entityManager;
    private final TopicRepository topicRepository;
    private final UserService userService;

    @Override
    public List<TopicDto> getTopic(SearchDto dto) {
        if (dto == null) {
            throw new NotFoundException("not found");
        }

        String whereClause = "";
        String orderBy = " ";
        String sql = "select e from Topic as e where(1=1) ";
        if (StringUtils.hasText(dto.getValueSearch())) {
            if ("ID".equals(dto.getConditionSearch())) {
                whereClause += " AND e.id = " + dto.getValueSearch();
            }
        }
        sql += whereClause + orderBy;
        Query q = entityManager.createQuery(sql, Topic.class);
        List<Topic> topics = q.getResultList();
        List<TopicDto> resultList = new ArrayList<>();
        topics.forEach(res -> resultList.add(TopicDto.of(res)));

        return resultList;
    }

    @Override
    public TopicDto addTopic(TopicDto dto) {
        if (dto != null) {
            Topic topic = Topic.builder()
                    .createdDate(LocalDateTime.now())
                    .name(dto.getName())
                    .createdBy(userService.getCurrentUser().getUsername())
                    .build();

            topicRepository.save(topic);

            return TopicDto.of(topic);
        }
        return null;
    }

    @Override
    public TopicDto editTopic(TopicDto dto) {
        if (dto.getId() == null) {
            throw new BadRequestException("Id is null");

        }
        Topic project = Topic.builder()
                .id(dto.getId())
                .name(dto.getName())
                .createdDate(dto.getCreatedDate())
                .createdBy(dto.getCreatedBy())
                .build();

        topicRepository.save(project);
        return TopicDto.of(project);
    }

    @Override
    public Boolean deleteTopic(Long id) {
        if (id != null) {
            Topic res = topicRepository.findById(id).orElseThrow(
                    () -> {
                        throw new NotFoundException("Not found topic with id: %d".formatted(id));
                    }
            );
            topicRepository.delete(res);
            return true;
        }
        return false;
    }
}
