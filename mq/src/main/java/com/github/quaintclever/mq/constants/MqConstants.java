package com.github.quaintclever.mq.constants;

/**
 * <p>
 * desc: mq 常量
 * </p>
 *
 * @author quaint
 * @since 02 December 2020
 */
public interface MqConstants {

    /**
     * routeKey 前缀
     */
    String ROUTE_KEY_PREFIX = "quaint.";


    /**
     * topic 交换机
     */
    String TOPIC_DEFAULT_EXCHANGE = "TOPIC_DEFAULT_EXCHANGE";

    /**
     * direct 交换机
     */
    String DIRECT_DEFAULT_EXCHANGE = "DIRECT_DEFAULT_EXCHANGE";

    /**
     * fanout 交换机
     */
    String FANOUT_DEFAULT_EXCHANGE = "FANOUT_DEFAULT_EXCHANGE";

    /**
     * headers 交换机
     */
    String HEADERS_DEFAULT_EXCHANGE = "HEADERS_DEFAULT_EXCHANGE";

}
