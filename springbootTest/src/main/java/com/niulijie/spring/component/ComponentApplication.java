package com.niulijie.spring.component;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author edz
 */
@SpringBootApplication
public class ComponentApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext = SpringApplication.run(ComponentApplication.class, args);
        String[] beanDefinitionNames = configurableApplicationContext.getBeanDefinitionNames();
        for (String beanName: beanDefinitionNames) {
            System.out.println(beanName);
        }
    }
}
