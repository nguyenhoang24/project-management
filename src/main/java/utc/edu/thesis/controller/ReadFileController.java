package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class ReadFileController {


    @GetMapping("/files")
    public ResponseEntity<byte[]> viewFile(@RequestParam String filePath) {
        try {
            Path path = Paths.get(filePath);
            byte[] fileContent = Files.readAllBytes(path);

            String fileExtension = getFileExtension(filePath).toLowerCase();
            MediaType mediaType = getMediaType(fileExtension);

            if (mediaType == null) {
                return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);

            // Thêm Content-Disposition để trình duyệt cố gắng hiển thị file nếu có thể
            if (mediaType.equals(MediaType.APPLICATION_PDF)) {
                headers.setContentDisposition(ContentDisposition.inline().build()); // Mở trực tiếp file PDF
            } else {
                headers.setContentDisposition(ContentDisposition.inline().filename(path.getFileName().toString()).build()); // Cho phép xem trực tiếp hoặc tải về
            }

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Các phương thức hỗ trợ vẫn giữ nguyên như trước
    private String getFileExtension(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filePath.substring(dotIndex + 1);
    }

    private MediaType getMediaType(String fileExtension) {
        switch (fileExtension) {
            case "pdf":
                return MediaType.APPLICATION_PDF;
            case "docx":
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            case "xlsx":
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "pptx":
                return MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation");
            default:
                return null;
        }
    }


}
