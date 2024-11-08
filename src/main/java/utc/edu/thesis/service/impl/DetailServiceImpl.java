package utc.edu.thesis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.DetailDto;
import utc.edu.thesis.domain.dto.EmailDetails;
import utc.edu.thesis.domain.dto.ProjectDto;
import utc.edu.thesis.domain.dto.SearchDto;
import utc.edu.thesis.domain.entity.Detail;
import utc.edu.thesis.domain.entity.Project;
import utc.edu.thesis.exception.request.NotFoundException;
import utc.edu.thesis.repository.DetailRepository;
import utc.edu.thesis.service.AmazonUploadService;
import utc.edu.thesis.service.DetailService;
import utc.edu.thesis.service.EmailService;
import utc.edu.thesis.service.ProjectService;

import java.util.ArrayList;
import java.util.List;

@Service
//@RequiredArgsConstructor
public class DetailServiceImpl implements DetailService {
    private final DetailRepository detailRepository;
    private final AmazonUploadService aws3Serivce;
    private final EmailService emailService;
    private final ProjectService projectService;

    private final String pathUrl;

    public DetailServiceImpl(DetailRepository detailRepository,
                             AmazonUploadService aws3Serivce,
                             EmailService emailService,
                             ProjectService projectService,
                             @Value("${upload.url}") String pathUrl) {
        this.detailRepository = detailRepository;
        this.aws3Serivce = aws3Serivce;
        this.emailService = emailService;
        this.projectService = projectService;
        this.pathUrl = pathUrl;
    }

    @Override
    public List<DetailDto> getDetail(Long id) {
        if (id != null) {
            List<Detail> details = detailRepository.getProjectDetail(id);
            List<DetailDto> res = new ArrayList<>();
            details.forEach(detail -> res.add(DetailDto.of(detail)));
            return res;
        }
        return null;
    }

    @Override
    public DetailDto getDetailById(Long id) {
        if (id != null) {
            return detailRepository.findById(id)
                    .map(DetailDto::of).orElseThrow(() -> {
                        throw new NotFoundException("Can not found detail with project id: %d".formatted(id));
                    });
        }
        return null;
    }

    @Override
    public DetailDto addDetail(DetailDto dto) {
        if (dto != null) {

            Detail detail = Detail.builder()
                    .title(dto.getTitle())
                    .status(dto.getStatus())
                    .comment(dto.getComment())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .reportFile(dto.getReportFile())
                    .project(dto.getProject())
                    .build();

            detailRepository.save(detail);
            return DetailDto.of(detail);
        }

        return null;
    }

    @Override
    public DetailDto editDetail(DetailDto dto) {
        if (dto != null) {
            Detail detail = detailRepository.findById(dto.getId())
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not found project with id: %d".formatted(dto.getId()));
                    });

            if(dto.getReportFile().isBlank()) {
                detail = Detail.builder()
                        .id(dto.getId())
                        .title(dto.getTitle())
                        .status(dto.getStatus())
                        .comment(dto.getComment())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .project(dto.getProject())
                        .build();
            } else {
                detail = Detail.builder()
                        .id(dto.getId())
                        .title(dto.getTitle())
                        .status(dto.getStatus())
                        .comment(dto.getComment())
                        .startDate(dto.getStartDate())
                        .endDate(dto.getEndDate())
                        .project(dto.getProject())
                        .reportFile(dto.getReportFile())
                        .build();
            }


            if(dto.getComment() != null) {
                ProjectDto project = projectService.getProjects(new SearchDto(String.valueOf(detail.getProject().getId()) , "ID")).get(0);
                EmailDetails emailDetails = EmailDetails.builder()
                        .msgBody(dto.getComment())
                        .recipient(new String[]{project.getStudent().getEmail()})
                        .subject("Giảng viên đã nhận xét về đồ án của bạn")
                        .build();
                emailService.sendSimpleMail(emailDetails);
            }

            detail = detailRepository.save(detail);
            return DetailDto.of(detail);
        }

        return null;
    }

    @Override
    public Boolean deleteDetail(Long id) {
        if (id != null) {
            Detail detail = detailRepository.findById(id)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not found project with id: %d".formatted(id));
                    });

            detailRepository.delete(detail);
            return true;
        }
        return false;
    }

    @Override
    public DetailDto deleteFileDetail(Long detailId) {
        if(detailId != null) {
            Detail detail = detailRepository.findById(detailId)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not find detail with id: %d".formatted(detailId));
                    });

            detail.setReportFile(null);
            detail = detailRepository.save(detail);
            return DetailDto.of(detail);
        }
        return null;
    }

    @Override
    public DetailDto addFileDetail(Long detailId, MultipartFile file, String type, String newFileName) {
        if (!file.isEmpty()) {
            Detail detail = detailRepository.findById(detailId)
                    .orElseThrow(() -> {
                        throw new NotFoundException("Can not find project with id: %lf".formatted(detailId));
                    });

//            String uploadFile = aws3Serivce.uploadFile(file);
            detail.setReportFile(pathUrl + "/api/files?filePath=uploads/" + newFileName);
            detail = detailRepository.save(detail);
            return DetailDto.of(detail);
        }
        return null;
    }
}
