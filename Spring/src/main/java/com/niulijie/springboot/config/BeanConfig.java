package com.niulijie.springboot.config;

import com.niulijie.springboot.entity.Demo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 在启动的时候先通过代码方式给spring容器中注入一个bean
 * @author niulijie
 */
@Configuration
public class BeanConfig {

    @Bean("testDemo")
    public Demo generateDemo(){
        Demo niuniu = Demo.builder().id(1).name("niuniu").build();
        return niuniu;
    }
}
