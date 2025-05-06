package org.example.message.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FanoutConfiguration {

//    @Bean
//    public FanoutExchange fanoutExchange() {
//        return ExchangeBuilder.fanoutExchange("mycloud.fanout").build();
//    }
//
//    @Bean
//    public Queue fanoutQueue1() {
//        return QueueBuilder.durable("fanout.queue1").build();
//    }
//    @Bean
//    public Queue fanoutQueue2() {
//        return QueueBuilder.durable("fanout.queue2").build();
//    }
//
//    @Bean
//    public Binding fanoutBinding(FanoutExchange fanoutExchange, Queue fanoutQueue1) {
//        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
//    }
//    @Bean
//    public Binding fanout2Binding(FanoutExchange fanoutExchange, Queue fanoutQueue2) {
//        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
//    }

    @Bean
    public DirectExchange errorExchange() {
        return new DirectExchange("error.direct");
    }

    @Bean
    public Queue errorQueue() {
        return new Queue("error");
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, DirectExchange errorExchange) {
        return BindingBuilder.bind(errorExchange).to(errorExchange).with("error");
    }

    @Bean
    public MessageRecoverer republishMessageRecoverer(RabbitTemplate rabbitTemplate) {
        return new RepublishMessageRecoverer(rabbitTemplate,"error.direct","error");
    }
}
