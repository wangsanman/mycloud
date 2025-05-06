package org.example.user.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class MqConfig {

    private final RabbitTemplate rabbitTemplate;

// todo 这里会报循环依赖异常
//    @Bean
//    public MessageConverter messageConverter() {
//        return new Jackson2JsonMessageConverter();
//    }


    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback((returned)->{
            log. error ("触发return callback,");
            log. debug ("exchange: {}", returned.getExchange());
            log. debug("routingKey: {}", returned.getRoutingKey());
            log. debug ("message: {}", returned.getMessage());
            log. debug("replyCode: {}", returned.getReplyCode());
            log. debug("replyText: {}", returned.getReplyText());
        });
    }
}
