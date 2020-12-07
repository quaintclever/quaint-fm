package com.github.quaintclever.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <p>
 * desc: swagger 配置文件
 * </p>
 *
 * @author quaint
 * @since 07 December 2020
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${spring.application.name}")
    String applicationName;

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .pathMapping("/")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.quaint.shop.member.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(new ApiInfoBuilder()
                        .title(applicationName)
                        .description(applicationName+" 整合Swagger")
                        .version("1.0")
                        .contact(new Contact("quaint","www.quaint-aini.com","123@qq.com"))
                        .build());
    }

}
