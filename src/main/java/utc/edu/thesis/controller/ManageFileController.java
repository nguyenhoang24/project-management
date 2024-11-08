package utc.edu.thesis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utc.edu.thesis.domain.dto.UploadImage;
import utc.edu.thesis.service.AmazonUploadService;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class ManageFileController {
    private final AmazonUploadService service;

    @PostMapping("/upload")
    public ResponseEntity<UploadImage> uploadFile(@RequestParam("upload")MultipartFile multipartFile) {
        return ResponseEntity.ok(service.uploadImg(multipartFile));
    }

//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile multipartFile) {
//        return ResponseEntity.ok(service.uploadFile(multipartFile));
//    }
}
