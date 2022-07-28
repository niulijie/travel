package com.niulijie.mdm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义拦截器配置
 * @author df
 * @date 2019/8/6
 */
@Configuration
public class CustomInterceptorConfig implements WebMvcConfigurer {

    private final HandlerInterceptor handlerInterceptor;

    @Autowired
    public CustomInterceptorConfig(HandlerInterceptor handlerInterceptor) {
        this.handlerInterceptor = handlerInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handlerInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/*.html")
                .excludePathPatterns("/static/**")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("//swagger-resources/**");
    }

}
