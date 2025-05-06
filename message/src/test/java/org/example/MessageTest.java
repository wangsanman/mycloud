package org.example;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Unit test for simple MessageApplication.
 */
@SpringBootTest
public class MessageTest
{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void setRabbitTemplate() {
        //指定队列
        String queueName = "simple.queue";
        //消息内容
        String message = "Hello xiaopohai";
        //发送消息
        rabbitTemplate.convertAndSend(queueName, message);

    }

}
