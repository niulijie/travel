package com.niulijie.springboot.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *  @Component
 *  @ConfigurationProperties(prefix = "mycar") 表示从默认的全局配置文件中获取值注入
 *  方法一：@ConfigurationProperties得添加@Component，使得Car成为容器中得组件，才会拥有SpringBoot提供的强大功能
 *  方法二：在配置类(BeanConfig)上添加@EnableConfigurationProperties(Car.class)
 *         1.开启car的配置绑定功能
 *         2.把car组件自动注册进容器中
 *  @PropertySource则是修改默认的全局配置文件，让@ConfigurationProperties去指定的配置文件获取值注入
 *      @PropertySource(value = {"classpath:person.properties"})
 */
@Data
@Component
@ConfigurationProperties(prefix = "mycar")
public class Car {

    private String brand;
    private Integer price;
}
