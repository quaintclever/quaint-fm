package com.github.quaintclever.core.annotation;

import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.annotation.AliasFor;
import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.annotation.*;

/**
 * <p>
 * desc: quaint 应用注解
 * </p>
 *
 * @author quaint
 * @since 19 November 2020
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringCloudApplication
@EnableAsync
//@EnableTransactionManagement
@ComponentScan
public @interface QuaintApplication {

    @AliasFor(annotation = ComponentScan.class)
    String basePackages();
}
