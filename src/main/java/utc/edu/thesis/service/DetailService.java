package utc.edu.thesis.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.DetailDto;

import java.util.List;

@Service
public interface DetailService {
    List<DetailDto> getDetail(Long id);
    DetailDto getDetailById(Long id);
    DetailDto addDetail(DetailDto dto);
    DetailDto editDetail(DetailDto dto);
    Boolean deleteDetail(Long id);
    DetailDto deleteFileDetail(Long detailId);
    DetailDto addFileDetail(Long detailId, MultipartFile file, String type, String newFileName);
}
