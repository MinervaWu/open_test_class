package com.example.demo.mybatis.pagehelper.configuration;

import com.example.demo.mybatis.pagehelper.aop.SeedPageHelperAspect;
import com.example.demo.mybatis.pagehelper.interceptor.SeedPageResultInterceptor;
import com.example.demo.mybatis.pagehelper.processor.SeedPageHelperPostProcessor;
import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(PageHelperAutoConfiguration.class)
public class SeedPageHelperAutoConfiguration {

    @Bean
    public SeedPageHelperAspect seedPageHelperAspect() {
        return new SeedPageHelperAspect();
    }

    @Bean
    public SeedPageResultInterceptor seedPageResultInterceptor() {
        return new SeedPageResultInterceptor();
    }

    @Bean
    public SeedPageHelperPostProcessor seedPageHelperPostProcessor() {
        return new SeedPageHelperPostProcessor();
    }
}
