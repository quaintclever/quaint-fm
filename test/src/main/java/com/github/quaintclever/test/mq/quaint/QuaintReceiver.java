//package com.github.quaintclever.test.mq.quaint;
//
//import com.github.quaintclever.mq.annotation.QuaintMessageListener;
//import com.github.quaintclever.mq.constants.MqConstants;
//import org.springframework.amqp.core.ExchangeTypes;
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
//public class QuaintReceiver {
//
//    @QuaintMessageListener
//    public void topicQueueListener(Object msg) {
//        System.out.println("quaint -> topicExchange1 ");
//        System.out.println(msg);
//    }
//
//    /**
//     * 指定队列
//     */
//    @QuaintMessageListener(queue = "topicQueueListener")
//    public void topicQueueListener2(Object msg) {
//        System.out.println("quaint -> topicExchange2 ");
//        System.out.println(msg);
//    }
//
//    @QuaintMessageListener(exchange = MqConstants.DIRECT_DEFAULT_EXCHANGE, exType = ExchangeTypes.DIRECT)
//    public void directQueueListener(Object msg) {
//        System.out.println("quaint -> directExchange1 ");
//        System.out.println(msg);
//    }
//
//    @QuaintMessageListener(exchange = MqConstants.FANOUT_DEFAULT_EXCHANGE, exType = ExchangeTypes.FANOUT)
//    public void fanoutQueueListener(Object msg) {
//        System.out.println("quaint -> fanoutExchange1 ");
//        System.out.println(msg);
//    }
//
//}
