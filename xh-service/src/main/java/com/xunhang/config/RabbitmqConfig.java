//package com.xunhang.config;
//
//import com.xunhang.constant.RabbitConstant;
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitmqConfig {
//
//
//
//
//
//    @Bean
//    Queue messageQueue() {
//        /**
//         * 创建队列，参数说明：
//         * String name：队列名称。
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化的队列会存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean exclusive：设置是否排他，默认也是 false。为 true 则设置队列为排他。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         * 当没有生产者或者消费者使用此队列，该队列会自动删除。
//         * Map<String, Object> arguments：设置队列的其他一些参数。
//         */
//        return new Queue(RabbitConstant.Message_QUEUE_NAME, true);
//    }
//
//    @Bean
//    DirectExchange messageExchange() {
//        /**
//         * 创建交换器，参数说明：
//         * String name：交换器名称
//         * boolean durable：设置是否持久化，默认是 false。durable 设置为 true 表示持久化，反之是非持久化。
//         * 持久化可以将交换器存盘，在服务器重启的时候不会丢失相关信息。
//         * boolean autoDelete：设置是否自动删除，为 true 则设置队列为自动删除，
//         */
//
//        return new DirectExchange(RabbitConstant.Message_EXCHANGE_NAME, true, false);
//    }
//
//    @Bean
//    Binding messageBinding() {
//        return BindingBuilder.bind(messageQueue()).to(messageExchange()).with(RabbitConstant.Message_ROUTING_KEY_NAME);
//    }
//
//}