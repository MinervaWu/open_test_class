package com.example.demo.mybatis.pagehelper.processor;

import com.github.pagehelper.autoconfigure.PageHelperProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Properties;

@Slf4j
public class SeedPageHelperPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof PageHelperProperties) {
            PageHelperProperties pageHelperProperties = (PageHelperProperties) bean;
            Properties properties = pageHelperProperties.getProperties();
            if (!properties.containsKey("")) {
                pageHelperProperties.setHelperDialect("mysql");
            }

        }
        return bean;
    }
}
