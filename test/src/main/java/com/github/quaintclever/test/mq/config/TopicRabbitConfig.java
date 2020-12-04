//package com.github.quaintclever.test.mq.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * <p>
// * desc: 主题交换机。
// * </p>
// *
// * @author quaint
// * @since 02 December 2020
// */
//@Configuration
//public class TopicRabbitConfig {
//
//    public final static String MAN = "topic.man";
//    public final static String WOMAN = "topic.woman";
//
//    @Bean
//    public Queue firstQueue() {
//        return new Queue(TopicRabbitConfig.MAN);
//    }
//
//    @Bean
//    public Queue secondQueue() {
//        return new Queue(TopicRabbitConfig.WOMAN);
//    }
//
//    @Bean
//    Binding bindingExchangeMessage(TopicExchange defaultTopicExchange) {
//        //将firstQueue和topicExchange绑定,而且绑定的键值为topic.MAN
//        //这样只要是消息携带的路由键是topic.MAN,才会分发到该队列
//        return BindingBuilder.bind(firstQueue()).to(defaultTopicExchange).with(MAN);
//    }
//
//    @Bean
//    Binding bindingExchangeMessage2(TopicExchange defaultTopicExchange) {
//        //将secondQueue和topicExchange绑定,而且绑定的键值为用上通配路由键规则topic.#
//        // 这样只要是消息携带的路由键是以topic.开头,都会分发到该队列
//        return BindingBuilder.bind(secondQueue()).to(defaultTopicExchange).with("topic.#");
//    }
//
//}
