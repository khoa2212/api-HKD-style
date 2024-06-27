package com.example.email_service.service;

import com.example.email_service.dto.GeneralResponseDTO;
import com.example.email_service.dto.SendMailDTO;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleMail(String recipientEmail, String subject, String content) throws MailSendException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hkd.style201@gmail.com");
        message.setSubject(subject);
        message.setTo(recipientEmail);
        message.setText(content);

        mailSender.send(message);
    }

}
