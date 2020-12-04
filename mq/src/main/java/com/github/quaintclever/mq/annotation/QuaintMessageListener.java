package com.github.quaintclever.mq.annotation;

import com.github.quaintclever.mq.constants.MqConstants;
import org.springframework.amqp.core.ExchangeTypes;

import java.lang.annotation.*;

/**
 * <p>
 * desc: 队列消息监听
 * {@link org.springframework.amqp.rabbit.annotation.RabbitListener}
 * </p>
 *
 * @author quaint
 * @since 02 December 2020
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QuaintMessageListener {

    /**
     * 交换机名称
     */
    String exchange() default MqConstants.TOPIC_DEFAULT_EXCHANGE;

    /**
     * 路由key, 默认: quaint. + ${queue}
     */
    String routeKey() default "";

    /**
     * 队列名, 默认: 方法名称
     */
    String queue() default "";

    /**
     * 交换机类型
     */
    String exType() default ExchangeTypes.TOPIC;

}
