package com.example.email_service.controller;

import com.example.email_service.dto.SendMailDTO;
import com.example.email_service.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    private final EmailService emailService;

    public MessageConsumer(EmailService emailService) {
        this.emailService = emailService;
    }
    @RabbitListener(queues = "user.forgot-password", messageConverter = "jsonMessageConverter")
    public void handleForgotPasswordMessage(@Payload SendMailDTO message) {
        emailService.sendSimpleMail(
                message.getRecipientEmail(),
                message.getSubject(),
                message.getContent()
        );
    }
}
