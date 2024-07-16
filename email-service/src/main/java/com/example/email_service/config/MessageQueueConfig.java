package com.example.email_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class MessageQueueConfig {
    @Bean
    public TopicExchange userExchange() {
        return new TopicExchange("user");
    }

    @Bean
    public Queue forgotPasswordQueue() {
        return new Queue("user.forgot-password");
    }

    @Bean
    public Binding forgotPasswordBinding(TopicExchange userExchange, Queue forgotPasswordQueue) {
        return BindingBuilder.bind(forgotPasswordQueue)
                .to(userExchange)
                .with("user.forgot-password");
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
