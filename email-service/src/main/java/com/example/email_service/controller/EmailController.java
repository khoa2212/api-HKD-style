package com.example.email_service.controller;

import com.example.email_service.dto.GeneralResponseDTO;
import com.example.email_service.dto.SendMailDTO;
import com.example.email_service.service.EmailService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping(
            path = "/api/mail",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<GeneralResponseDTO> sendMail(@RequestBody SendMailDTO request) {
        emailService.sendSimpleMail(
                request.getRecipientEmail(),
                request.getSubject(),
                request.getContent());

        return ResponseEntity.ok(new GeneralResponseDTO("Sent email successfully"));
    }

}
