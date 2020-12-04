//package com.github.quaintclever.test.mq.quaint;
//
//import com.github.quaintclever.mq.constants.MqConstants;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * <p>
// * desc:
// * </p>
// *
// * @author quaint
// * @since 02 December 2020
// */
//@RestController
//@RequestMapping("quaint")
//public class QuaintSendMessageController {
//
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//
//    @GetMapping("sendMessage")
//    public String sendTopicMessage() {
//        rabbitTemplate.convertAndSend(MqConstants.TOPIC_DEFAULT_EXCHANGE,"quaint.topicQueueListener","hello, this message from quaint! topicExchange1");
//        rabbitTemplate.convertAndSend(MqConstants.DIRECT_DEFAULT_EXCHANGE,"quaint.directQueueListener","hello, this message from quaint! directExchange1");
//        rabbitTemplate.convertAndSend(MqConstants.FANOUT_DEFAULT_EXCHANGE,"quaint.fanoutQueueListener","hello, this message from quaint! fanoutExchange1");
//        return "ok";
//    }
//
//}
