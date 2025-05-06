package org.example.message.mq;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class SpringRabbitListener {

    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueue(String message) {
        log.info("消费者1监听到的消息:[{}], 当前时间:{}" , message , LocalDateTime.now());
    }

    @RabbitListener(queues = "work.queue")
    public void workSimpleQueue1(String message) {
        log.info("消费者1监听到的消息:[{}], 当前时间:{}" , message , LocalDateTime.now());
        ThreadUtil.sleep(50);
    }

    @RabbitListener(queues = "work.queue")
    public void workSimpleQueue2(String message) {
        log.info("消费者2..............监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
        ThreadUtil.sleep(200);
    }

    @RabbitListener(queues = "fanout.queue1")
    public void fanoutQueue1(String message) {
        log.info("消费者1 监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }

    @RabbitListener(queues = "fanout.queue2")
    public void fanoutQueue2(String message) {
        log.info("消费者2..............监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }
    @RabbitListener(queues = "direct.queue1")
    public void directQueue1(String message) {
        log.info("消费者1 监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }

    @RabbitListener(queues = "direct.queue2")
    public void directQueue2(String message) {
        log.info("消费者2..............监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }

    @RabbitListener(queues = "topic.queue1")
    public void topicQueue1(String message) {
        log.info("消费者1 监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }

    @RabbitListener(queues = "topic.queue2")
    public void topicQueue2(String message) {
        log.info("消费者2..............监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue("direct.queue"),
            exchange = @Exchange("mycloud.direct"),
            key = {"blue","yellow"}
    ),@QueueBinding(
            value = @Queue("direct.queue"),
            exchange = @Exchange(value = "mycloud.dir",type = ExchangeTypes.DIRECT),
            key = {"red","white"}
    )})
    public void directQueue(String message) {
        log.info("a a a消费者1 监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());
    }

    @RabbitListener(bindings = {@QueueBinding(
            value = @Queue("direct.queue"),
            exchange = @Exchange(value = "mycloud.direct",type = ExchangeTypes.DIRECT),
            key = {"black"}
    )})
    public void directQueue3(String message) {
        log.info("3333消费者1 监听到的消息:[{}], 当前时间:{}" , message ,  LocalDateTime.now());

    }
}
