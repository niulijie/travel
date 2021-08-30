package com.niulijie.common.config;

import com.niulijie.common.intercepter.AccessInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.mdm.common.config
 * @email zhoupengbing@telecomyt.com.cn
 * @description
 * @createTime 2020年03月04日 15:39:00 @Version v1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessInterceptor());
    }
}
