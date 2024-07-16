package com.example.user_service.service;

import com.example.user_service.constant.PublisherConst;
import com.example.user_service.dto.SendEmailRequestDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {
    private final AmqpTemplate amqpTemplate;

    public MessageProducerService(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void sendForgotPasswordMessage(SendEmailRequestDTO message) {
        amqpTemplate.convertAndSend(PublisherConst.USER_EXCHANGE, PublisherConst.FORGOT_PASSWORD_EVENT, message);
    }
}
