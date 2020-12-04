//package com.github.quaintclever.test.mq.listener;
//
//import com.github.quaintclever.test.mq.dto.MessageDto;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
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
//public class FanoutReceiver {
//
//    @RabbitHandler
//    @RabbitListener(queues = "fanout.A")
//    public void processA(MessageDto testMessage) {
//        System.out.println("FanoutReceiverA消费者收到消息  : " +testMessage);
//    }
//
//    @RabbitHandler
//    @RabbitListener(queues = "fanout.B")
//    public void processB(MessageDto testMessage) {
//        System.out.println("FanoutReceiverB消费者收到消息  : " +testMessage);
//    }
//
//    @RabbitHandler
//    @RabbitListener(queues = "fanout.C")
//    public void processC(MessageDto testMessage) {
//        System.out.println("FanoutReceiverC消费者收到消息  : " +testMessage);
//    }
//
//}
