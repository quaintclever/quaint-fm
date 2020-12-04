package com.github.quaintclever.mq.annotation;

import com.github.quaintclever.mq.constants.MqConstants;
import com.github.quaintclever.mq.listener.QuaintMethodRabbitListenerEndpoint;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerAnnotationBeanPostProcessor;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.RabbitListenerConfigUtils;
import org.springframework.amqp.rabbit.listener.MethodRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.MimeType;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * desc: 队列消息事件bean后置处理
 *
 * 参考 {@link RabbitListenerAnnotationBeanPostProcessor}
 * </p>
 *
 * @author quaint
 * @since 02 December 2020
 */
@Component
public class QuaintMessageListenerBeanPostProcessor implements ApplicationContextAware, BeanPostProcessor, SmartInitializingSingleton {

    @Value("${spring.application.name}")
    private String serviceName;

    private ApplicationContext applicationContext;

    private final RabbitListenerEndpointRegistrar registrar = new RabbitListenerEndpointRegistrar();

    private RabbitListenerEndpointRegistry endpointRegistry;

    private String containerFactoryBeanName = RabbitListenerAnnotationBeanPostProcessor.DEFAULT_RABBIT_LISTENER_CONTAINER_FACTORY_BEAN_NAME;

    private final RabbitHandlerMethodFactoryAdapter messageHandlerMethodFactory = new RabbitHandlerMethodFactoryAdapter();

    private int increment;

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {

        this.registrar.setBeanFactory(this.applicationContext);
        Map<String, RabbitListenerConfigurer> instances = applicationContext.getBeansOfType(RabbitListenerConfigurer.class);
        for (RabbitListenerConfigurer configurer : instances.values()) {
            configurer.configureRabbitListeners(this.registrar);
        }

        if (this.registrar.getEndpointRegistry() == null) {
            if (this.endpointRegistry == null) {
                Assert.state(this.applicationContext != null,
                        "BeanFactory must be set to find endpoint registry by bean name");
                this.endpointRegistry = this.applicationContext.getBean(
                        RabbitListenerConfigUtils.RABBIT_LISTENER_ENDPOINT_REGISTRY_BEAN_NAME,
                        RabbitListenerEndpointRegistry.class);
            }
            this.registrar.setEndpointRegistry(this.endpointRegistry);
        }

        if (this.containerFactoryBeanName != null) {
            this.registrar.setContainerFactoryBeanName(this.containerFactoryBeanName);
        }

        // Set the custom handler method factory once resolved by the configurer
        MessageHandlerMethodFactory handlerMethodFactory = this.registrar.getMessageHandlerMethodFactory();
        if (handlerMethodFactory != null) {
            this.messageHandlerMethodFactory.setMessageHandlerMethodFactory(handlerMethodFactory);
        }
        this.registrar.afterPropertiesSet();
    }


    @Override
    public Object postProcessAfterInitialization(@NonNull Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, method -> {
            QuaintMessageListener quaintMessageListener = AnnotationUtils.findAnnotation(method, QuaintMessageListener.class);
            if(quaintMessageListener != null){
                this.processListener(quaintMessageListener,method,bean);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }

    private void processListener(QuaintMessageListener quaintMessageListener, Method method, Object bean){
        Method methodToUse = checkProxy(method, bean);
        MethodRabbitListenerEndpoint endpoint = new QuaintMethodRabbitListenerEndpoint(serviceName);
        endpoint.setMethod(methodToUse);
        endpoint.setBean(bean);
        endpoint.setMessageHandlerMethodFactory(this.messageHandlerMethodFactory);
        endpoint.setId(QuaintMessageListenerBeanPostProcessor.class.getName() + "#"+ this.counter.getAndIncrement());
        endpoint.setBeanFactory(this.applicationContext);
        endpoint.setReturnExceptions(false);
        endpoint.setExclusive(false);

        // 设置队列, 默认方法名称
        String queueName;
        if (StringUtils.isEmpty(quaintMessageListener.queue().trim())) {
            queueName = method.getName();
        } else {
            queueName = quaintMessageListener.queue();
        }
        endpoint.setQueueNames(queueName);

        // 设置 路由键, 默认 => quaint.${methodName}
        String routingKey;
        if(StringUtils.isEmpty(quaintMessageListener.routeKey().trim())){
            routingKey = MqConstants.ROUTE_KEY_PREFIX + queueName;
        }else{
            routingKey = quaintMessageListener.routeKey();
        }

        declareAndBindQueue(queueName,routingKey, quaintMessageListener.exchange(), quaintMessageListener.exType());
        this.registrar.registerEndpoint(endpoint);
    }

    private void declareAndBindQueue(String queueName,String routingKey,String exchangeName, String exchangeType) {
        //创建队列并注册到spring容器中
        Queue queue = new Queue(queueName,true,false,false);
        queue.setIgnoreDeclarationExceptions(false);
        queue.setShouldDeclare(true);
        ((ConfigurableBeanFactory)this.applicationContext.getAutowireCapableBeanFactory())
                .registerSingleton(queueName + ++this.increment,queue);

        //创建交换机并注册到spring容器中
        ExchangeBuilder exchangeBuilder = new ExchangeBuilder(exchangeName,exchangeType);
        exchangeBuilder.durable(true);
        Exchange exchange = exchangeBuilder.build();
        ((ConfigurableBeanFactory)this.applicationContext.getAutowireCapableBeanFactory())
                .registerSingleton(exchangeName + ++this.increment,exchange);

        //创建绑定并注册到spring容器中
        Binding actualBinding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);
        actualBinding.setShouldDeclare(true);
        actualBinding.setIgnoreDeclarationExceptions(true);
        ((ConfigurableBeanFactory)this.applicationContext.getAutowireCapableBeanFactory())
                .registerSingleton(exchangeName + "." + queueName + ++this.increment,actualBinding);

    }

    private Method checkProxy(Method method, Object bean) {
        if (AopUtils.isJdkDynamicProxy(bean)) {
            try {
                // Found a @RabbitListener method on the target class for this JDK proxy ->
                // is it also present on the proxy itself?
                method = bean.getClass().getMethod(method.getName(), method.getParameterTypes());
                Class<?>[] proxiedInterfaces = ((Advised) bean).getProxiedInterfaces();
                for (Class<?> iface : proxiedInterfaces) {
                    try {
                        method = iface.getMethod(method.getName(), method.getParameterTypes());
                        break;
                    }
                    catch (NoSuchMethodException noMethod) {
                    }
                }
            }
            catch (SecurityException ex) {
                ReflectionUtils.handleReflectionException(ex);
            }
            catch (NoSuchMethodException ex) {
                throw new IllegalStateException(String.format(
                        "@QueueMessageEventListener method '%s' found on bean target class '%s', " +
                                "but not found in any interface(s) for bean JDK proxy. Either " +
                                "pull the method up to an interface or switch to subclass (CGLIB) " +
                                "proxies by setting proxy-target-class/proxyTargetClass " +
                                "attribute to 'true'", method.getName(), method.getDeclaringClass().getSimpleName()));
            }
        }
        return method;
    }


    /**
     * An {@link MessageHandlerMethodFactory} adapter that offers a configurable underlying
     * instance to use. Useful if the factory to use is determined once the endpoints
     * have been registered but not created yet.
     * @see RabbitListenerEndpointRegistrar#setMessageHandlerMethodFactory
     */
    private class RabbitHandlerMethodFactoryAdapter implements MessageHandlerMethodFactory {

        private MessageHandlerMethodFactory messageHandlerMethodFactory;

        RabbitHandlerMethodFactoryAdapter() {
            super();
        }

        public void setMessageHandlerMethodFactory(MessageHandlerMethodFactory rabbitHandlerMethodFactory1) {
            this.messageHandlerMethodFactory = rabbitHandlerMethodFactory1;
        }

        @Override
        public InvocableHandlerMethod createInvocableHandlerMethod(Object bean, Method method) {
            return getMessageHandlerMethodFactory().createInvocableHandlerMethod(bean, method);
        }

        private MessageHandlerMethodFactory getMessageHandlerMethodFactory() {
            if (this.messageHandlerMethodFactory == null) {
                this.messageHandlerMethodFactory = createDefaultMessageHandlerMethodFactory();
            }
            return this.messageHandlerMethodFactory;
        }

        private MessageHandlerMethodFactory createDefaultMessageHandlerMethodFactory() {
            DefaultMessageHandlerMethodFactory defaultFactory = new DefaultMessageHandlerMethodFactory();
            defaultFactory.setBeanFactory(QuaintMessageListenerBeanPostProcessor.this.applicationContext);

            //设置jackson messageConverter
            List<MimeType> mimeTypeList = new ArrayList<>();
            mimeTypeList.add(new MimeType("application", "json", StandardCharsets.UTF_8));
            mimeTypeList.add(new MimeType("text", "plain", StandardCharsets.UTF_8));
            MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter(mimeTypeList.toArray(new MimeType[mimeTypeList.size()]));
            mappingJackson2MessageConverter.setObjectMapper(Jackson2ObjectMapperBuilder.json().build());

            defaultFactory.setMessageConverter(mappingJackson2MessageConverter);
            defaultFactory.afterPropertiesSet();
            return defaultFactory;
        }
    }

}
