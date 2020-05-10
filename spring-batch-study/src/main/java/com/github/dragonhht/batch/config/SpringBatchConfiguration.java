package com.github.dragonhht.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.ApplicationContextFactory;
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Batch配置.
 *
 * @author: huang
 * @Date: 2019-4-12
 */
@Configuration
// 打开Batch。多job时，将modular设置为true
@EnableBatchProcessing(modular = true)
public class SpringBatchConfiguration {

    /**
     * 加载保存用户的Job.
     * @return
     */
    @Bean
    public ApplicationContextFactory saveUserJobContext() {
        return new GenericApplicationContextFactory(SaveUserJobConfig.class);
    }

}
