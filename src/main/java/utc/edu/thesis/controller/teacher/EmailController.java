// Java Program to Create Rest Controller that
// Defines various API for Sending Mail

package utc.edu.thesis.controller.teacher;

// Importing required classes

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import utc.edu.thesis.domain.dto.EmailDetails;
import utc.edu.thesis.service.EmailService;

// Annotation
@RestController
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    // Sending a simple Email
    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody EmailDetails details) {
        return ResponseEntity.ok(emailService.sendSimpleMail(details));
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public ResponseEntity<String> sendMailWithAttachment(
            @RequestBody EmailDetails details) {
		return ResponseEntity.ok(emailService.sendMailWithAttachment(details));
    }
}
