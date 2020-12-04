package com.github.quaintclever.mq.listener;

import com.github.quaintclever.mq.listener.adapter.QuaintMessagingMessageListenerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.adapter.MessagingMessageListenerAdapter;

/**
 * <p>
 * desc: rabbit listener endpoint
 *
 * {@link MethodRabbitListenerEndpoint}
 * </p>
 *
 * @author quaint
 * @since 02 December 2020
 */
@Slf4j
public class QuaintMethodRabbitListenerEndpoint extends MethodRabbitListenerEndpoint {

    /**
     * 服务名称
     */
    private String serviceName;

    public QuaintMethodRabbitListenerEndpoint(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    protected MessagingMessageListenerAdapter createMessageListenerInstance() {
        return new QuaintMessagingMessageListenerAdapter(this.getBean(), this.getMethod(), serviceName);
    }

}
