package utc.edu.thesis.service;

import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.S3File;
import utc.edu.thesis.domain.dto.UploadImage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface AmazonUploadService {
    void uploadFileTos3bucket(String fileName, File file);
    File convertMultiPartToFile(MultipartFile file) throws IOException;
    String generateFileName(MultipartFile multiPart);
    String uploadFile(MultipartFile multiPart);
    List<S3File> listFile();
    UploadImage uploadImg(MultipartFile multipartFile);
}
