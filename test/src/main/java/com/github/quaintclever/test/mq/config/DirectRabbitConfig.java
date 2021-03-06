//package com.github.quaintclever.test.mq.config;
//
//import org.springframework.amqp.core.BindingBuilder;
//import org.springframework.amqp.core.DirectExchange;
//import org.springframework.amqp.core.Queue;
//import org.springframework.amqp.core.Binding;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * <p>
// * desc: 直连型交换机 测试
// * 是实现了轮询的方式对消息进行消费，而且不存在重复消费。
// * </p>
// *
// * @author quaint
// * @since 01 December 2020
// */
//@Configuration
//public class DirectRabbitConfig {
//
//    @Bean
//    public Queue TestDirectQueue() {
//        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
//        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
//        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
//        //   return new Queue("TestDirectQueue",true,true,false);
//
//        //一般设置一下队列的持久化就好,其余两个就是默认false
//        return new Queue("directQueue",true);
//    }
//
//    @Bean
//    Binding bindingDirect(@Qualifier("defaultDirectExchange") DirectExchange defaultDirectExchange) {
//        //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
//        return BindingBuilder.bind(TestDirectQueue()).to(defaultDirectExchange).with("directRouting");
//    }
//
//}
