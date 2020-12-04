//package com.github.quaintclever.test.mq.listener;
//
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * <p>
// * desc:
// * </p>
// *
// * @author quaint
// * @since 02 December 2020
// */
//@Component
//public class TopicTotalReceiver {
//
//    @RabbitHandler
//    @RabbitListener(queues = "topic.man")
//    public void processMan(Map testMessage) {
//        System.out.println("processMan消费者收到消息  : " + testMessage.toString());
//    }
//
//    @RabbitHandler
//    @RabbitListener(queues = "topic.woman")
//    public void processWoman(String testMessage) {
//        System.out.println("processWoman消费者收到消息  : " + testMessage);
//    }
//
//}
