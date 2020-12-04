//package com.github.quaintclever.test.mq.config;
//
//import org.springframework.amqp.core.Binding;
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.FanoutExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * <p>
// * desc: 广播交换机
// * </p>
// *
// * @author quaint
// * @since 02 December 2020
// */
//@Configuration
//public class FanoutRabbitConfig {
//
//    /**
//     *  创建三个队列 ：fanout.A   fanout.B  fanout.C
//     *  将三个队列都绑定在交换机 fanoutExchange 上
//     *  因为是广播交换机, 路由键无需配置,配置也不起作用
//     */
//    @Bean
//    public Queue queueA() {
//        return new Queue("fanout.A");
//    }
//
//    @Bean
//    public Queue queueB() {
//        return new Queue("fanout.B");
//    }
//
//    @Bean
//    public Queue queueC() {
//        return new Queue("fanout.C");
//    }
//
//
//    @Bean
//    Binding bindingExchangeA(FanoutExchange defaultFanoutExchange) {
//        return BindingBuilder.bind(queueA()).to(defaultFanoutExchange);
//    }
//
//    @Bean
//    Binding bindingExchangeB(FanoutExchange defaultFanoutExchange) {
//        return BindingBuilder.bind(queueB()).to(defaultFanoutExchange);
//    }
//
//    @Bean
//    Binding bindingExchangeC(FanoutExchange defaultFanoutExchange) {
//        return BindingBuilder.bind(queueC()).to(defaultFanoutExchange);
//    }
//}
