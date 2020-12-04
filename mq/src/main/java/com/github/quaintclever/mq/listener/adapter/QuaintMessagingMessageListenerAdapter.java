package com.github.quaintclever.mq.listener.adapter;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * desc: 消息侦听器适配器
 * </p>
 *
 * @author quaint
 * @since 03 December 2020
 */
@Slf4j
public class QuaintMessagingMessageListenerAdapter extends MessagingMessageListenerAdapter{

    /**
     * 服务名称
     */
    private String serviceName;

    public QuaintMessagingMessageListenerAdapter(Object bean, Method method, String serviceName) {
        super(bean, method);
        this.serviceName = serviceName;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        String msgStr = new String(message.getBody(), StandardCharsets.UTF_8);
        try {
            log.info("【 ===> {}服务 <=== 】从队列 [{}] 接收到消息: \n{}", serviceName,
                    message.getMessageProperties().getConsumerQueue(), msgStr);
            super.onMessage(message, channel);
            ackMessage(message, channel);
            log.info("消息消费成功!");
        } catch (Exception e) {
            // 也可以捕获自己的业务异常
            log.error(e.getMessage(), e);
            rejectMessage(message, channel);
            log.info("消息消费失败, msg:{}",msgStr);
        }
    }

    /**
     * 确认消息
     * @param amqpMessage
     * @param channel
     */
    private void ackMessage(Message amqpMessage, Channel channel) {
        try {
            //ack确认消息
            channel.basicAck(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 解决消息
     * @param amqpMessage
     * @param channel
     */
    private void rejectMessage(Message amqpMessage, Channel channel) {
        try {
            //丢弃消息
            channel.basicReject(amqpMessage.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
