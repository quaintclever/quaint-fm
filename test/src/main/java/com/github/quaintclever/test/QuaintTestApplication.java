package com.github.quaintclever.test;

import com.github.quaintclever.core.annotation.QuaintApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * <p>
 * desc:
 * </p>
 *
 * @author quaint
 * @since 19 November 2020
 */
@QuaintApplication(basePackages = "com.github.quaintclever")
@EnableElasticsearchRepositories(basePackages = "com.github.quaintclever")
public class QuaintTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuaintTestApplication.class);
    }

}
