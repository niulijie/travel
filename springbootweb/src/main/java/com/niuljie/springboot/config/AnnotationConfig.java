package com.niuljie.springboot.config;

import com.niuljie.springboot.dto.Pet;
import com.niuljie.springboot.service.ServiceTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * 配置类=配置文件,告诉spring这是一个配置类
 * excludeFilters=Filter[];指定扫描的时候按照什么规则排除那些组件
 * includeFilters=Filter[];指定扫描的时候只需要包含那些组件,需要关联useDefaultFilters = false使用
 * FilterType.ANNOTATION --> 按照注解 @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class, Service.class})
 * FilterType.ASSIGNABLE_TYPE --> 按照给定类型 @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ServiceTest.class})
 * FilterType.ASPECTJ --> 按照ASPECTJ表达式
 * FilterType.REGEX --> 按照正则表达式
 * FilterType.CUSTOM --> 按照自定义规则 @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class})
 *
 */
@Configuration
@ComponentScan(value = "com.niuljie.springboot.*", includeFilters = {
        /*@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = {Controller.class, Service.class}),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {ServiceTest.class}),*/
        @ComponentScan.Filter(type = FilterType.CUSTOM, classes = {MyTypeFilter.class})
}, useDefaultFilters = false)
public class AnnotationConfig {

    //给容器中注册一个bean，类型为返回值的类型，id默认是方法名作为id
    @Bean(value = "pet")
    public Pet pet1(){
        return new Pet("lisi", 23);
    }
}
