package com.xunhang.config;

import com.xunhang.constant.RabbitConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {





    @Bean
    Queue messageQueue() {
        return new Queue(RabbitConstant.Message_QUEUE_NAME, true);
    }

    @Bean
    DirectExchange messageExchange() {
        return new DirectExchange(RabbitConstant.Message_EXCHANGE_NAME, true, false);
    }

    @Bean
    Binding messageBinding() {
        return BindingBuilder.bind(messageQueue()).to(messageExchange()).with(RabbitConstant.Message_ROUTING_KEY_NAME);
    }

}