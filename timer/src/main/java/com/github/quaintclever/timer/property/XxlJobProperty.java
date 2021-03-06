package com.github.quaintclever.timer.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * desc: xxl-job 配置属性
 * </p>
 *
 * 文档:
 * https://github.com/xuxueli/xxl-job/releases/tag/v2.2.0
 *
 * @author quaint
 * @since 19 November 2020
 */
@ConfigurationProperties(prefix = "xxl.job")
@Data
public class XxlJobProperty {

    private AdminProperties admin = new AdminProperties();
    private String accessToken;
    private ExecutorProperties executor = new ExecutorProperties();

    @Data
    public class AdminProperties{
        // 调度中心地址
        private String addresses;
        // 执行器名称
        private String appname;
    }

    @Data
    public class ExecutorProperties{
        private String ip = "";
        private String address = "";
        private int port = -1;
        private String logPath = "logs/jobhanlder";
        private int logRetentionDays = -1;
    }

}
