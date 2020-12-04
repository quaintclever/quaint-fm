//package com.github.quaintclever.test.mq.controller;
//
//import com.github.quaintclever.test.mq.dto.MessageDto;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * <p>
// * desc: 发送消息 测试
// * </p>
// *
// * @author quaint
// * @since 01 December 2020
// */
//@RestController
//public class SendMessageController {
//
//    @Autowired
//    RabbitTemplate rabbitTemplate;
//
//    @SuppressWarnings("Duplicates")
//    @GetMapping("/sendDirectMessage")
//    public String sendDirectMessage() {
//        String messageId = String.valueOf(UUID.randomUUID());
//        String messageData = "test message, hello!";
//        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Map<String,Object> map=new HashMap<>();
//        map.put("messageId",messageId);
//        map.put("messageData",messageData);
//        map.put("createTime",createTime);
//        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange
//        rabbitTemplate.convertAndSend("directExchange", "directRouting", map);
//        return "direct ok";
//    }
//
//
//    @SuppressWarnings("Duplicates")
//    @GetMapping("/sendTopicMessage1")
//    public String sendTopicMessage1() {
//        String messageId = String.valueOf(UUID.randomUUID());
//        String messageData = "message: M A N ";
//        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Map<String, Object> manMap = new HashMap<>();
//        manMap.put("messageId", messageId);
//        manMap.put("messageData", messageData);
//        manMap.put("createTime", createTime);
//        rabbitTemplate.convertAndSend("topicExchange", "topic.man", manMap);
//        return "topic1 ok";
//    }
//
//    @SuppressWarnings("Duplicates")
//    @GetMapping("/sendTopicMessage2")
//    public String sendTopicMessage2() {
//        String messageId = String.valueOf(UUID.randomUUID());
//        String messageData = "message: woman is all ";
//        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Map<String, Object> womanMap = new HashMap<>();
//        womanMap.put("messageId", messageId);
//        womanMap.put("messageData", messageData);
//        womanMap.put("createTime", createTime);
//        rabbitTemplate.convertAndSend("topicExchange", "topic.woman", womanMap);
//        return "topic2 ok";
//    }
//
//    @GetMapping("/sendFanoutMessage")
//    public String sendFanoutMessage() {
//        MessageDto messageDto = new MessageDto();
//        messageDto.setMessageId(String.valueOf(UUID.randomUUID()));
//        messageDto.setMessageData("message: fanout hello!");
//        messageDto.setCreateTime(LocalDateTime.now());
//        rabbitTemplate.convertAndSend("fanoutExchange",null, messageDto);
//        return "fanout ok";
//    }
//
//    @SuppressWarnings("Duplicates")
//    @GetMapping("/TestMessageAck")
//    public String TestMessageAck() {
//        String messageId = String.valueOf(UUID.randomUUID());
//        String messageData = "message: non-existent-exchange test message ";
//        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        Map<String, Object> map = new HashMap<>(16);
//        map.put("messageId", messageId);
//        map.put("messageData", messageData);
//        map.put("createTime", createTime);
//        rabbitTemplate.convertAndSend("non-existent-exchange", "TestDirectRouting", map);
//        return "ok";
//    }
//
//
//}
