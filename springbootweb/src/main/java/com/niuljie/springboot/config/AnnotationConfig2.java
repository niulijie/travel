package com.niuljie.springboot.config;

import com.niuljie.springboot.dto.Pet;
import org.springframework.context.annotation.*;

/**
 *
 */
@Configuration
public class AnnotationConfig2 {

    /**
     * ConfigurableBeanFactory.SCOPE_PROTOTYPE,  prototype  多实例
     * ConfigurableBeanFactory.SCOPE_SINGLETON,  singleton  单实例(默认值)
     * org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST, request 同一次请求创建一个实例
     * org.springframework.web.context.WebApplicationContext.SCOPE_SESSION,  session 同一个session创建一个实例
     * @return
     */
    @Scope("prototype")
    //默认是单实例的
    @Bean(value = "pet2")
    public Pet pet(){
        return new Pet("张三", 23);
    }
}
