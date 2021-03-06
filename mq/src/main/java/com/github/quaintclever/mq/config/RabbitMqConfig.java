package com.github.quaintclever.mq.config;

import com.github.quaintclever.mq.constants.MqConstants;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * <p>
 * desc: rabbit mq 相关配置
 * </p>
 *
 * @author quaint
 * @since 20 November 2020
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    /**
     * rabbitTemplate中用来转换json消息(jackson support)
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter(Jackson2ObjectMapperBuilder.json().build());
    }

    /**
     * 如果想要设置不同的回调类，就要设置为prototype的scope。
     * 并且 setConfirmCallback setReturnCallback 由 子类实现
     * //    @Scope(BeanDefinition.SCOPE_PROTOTYPE)
     * @param connectionFactory
     * @param messageConverter
     * @return
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            System.out.println("ConfirmCallback:     "+"相关数据："+correlationData);
            System.out.println("ConfirmCallback:     "+"确认情况："+ack);
            System.out.println("ConfirmCallback:     "+"原因："+cause);
        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("ReturnCallback:     "+"消息："+message);
            System.out.println("ReturnCallback:     "+"回应码："+replyCode);
            System.out.println("ReturnCallback:     "+"回应信息："+replyText);
            System.out.println("ReturnCallback:     "+"交换机："+exchange);
            System.out.println("ReturnCallback:     "+"路由键："+routingKey);
        });
        return rabbitTemplate;
    }

    /**
     * 注册默认交换机
     */
    @Bean
    @ConditionalOnMissingBean
    DirectExchange defaultDirectExchange() {
        return new DirectExchange(MqConstants.DIRECT_DEFAULT_EXCHANGE);
    }

    @Bean
    @ConditionalOnMissingBean
    FanoutExchange defaultFanoutExchange() {
        return new FanoutExchange(MqConstants.FANOUT_DEFAULT_EXCHANGE);
    }

    @Bean
    @ConditionalOnMissingBean
    TopicExchange defaultTopicExchange() {
        return new TopicExchange(MqConstants.TOPIC_DEFAULT_EXCHANGE);
    }



}