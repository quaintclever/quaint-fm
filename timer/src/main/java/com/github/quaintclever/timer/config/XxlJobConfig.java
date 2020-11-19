package com.github.quaintclever.timer.config;

import com.github.quaintclever.timer.property.XxlJobProperty;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * <p>
 * desc: xxl 定时任务配置类
 *
 * 文档:
 * https://github.com/xuxueli/xxl-job/releases/tag/v2.2.0
 * </p>
 *
 * @author quaint
 * @since 19 November 2020
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "xxl.job.admin",name = "addresses")
@EnableConfigurationProperties(XxlJobProperty.class)
public class XxlJobConfig {

    @Autowired
    XxlJobProperty xxlJobProperty;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean(initMethod = "start", destroyMethod = "destroy")
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info("【 ===> xxlJobExecutor <=== 】method start! 配置初始化开始~");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperty.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(StringUtils.isEmpty(xxlJobProperty.getAdmin().getAppname())
                ? applicationName
                : xxlJobProperty.getAdmin().getAppname());
        xxlJobSpringExecutor.setAddress(xxlJobProperty.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(xxlJobProperty.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobProperty.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperty.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobProperty.getExecutor().getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobProperty.getExecutor().getLogRetentionDays());
        log.info("【 ===> xxlJobExecutor <=== 】method end");
        return xxlJobSpringExecutor;
    }

}
