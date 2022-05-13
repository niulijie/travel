package com.atguigu.admin.config;

import com.atguigu.admin.interceptor.LoginInterceptor;
import com.atguigu.admin.interceptor.RedisUrlCountInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 1、编写一个拦截器实现HandlerInterceptor接口
 * 2、拦截器注册到容器中（实现WebMvcConfigurer的addInterceptors）
 * 3、指定拦截规则【如果是拦截所有，静态资源也会被拦截】
 * --------------------------------------------------------------------------
 * 1、@EnableWebMvc + WebMvcConfigurer —— @Bean  可以全面接管SpringMVC，所有规则全部自己重新配置
 *      静态资源，视图解析器，欢迎页--全面失效
 * 2、原理
 *   ○ 1、WebMvcAutoConfiguration  默认的SpringMVC的自动配置功能类。静态资源、欢迎页.....
 *   ○ 2、一旦使用 @EnableWebMvc, 会 @Import(DelegatingWebMvcConfiguration.class)
 *   ○ 3、DelegatingWebMvcConfiguration的作用，只保证SpringMVC最基本的使用
 *        ■ 把所有系统中的 WebMvcConfigurer 拿过来。所有功能的定制都是这些 WebMvcConfigurer合起来一起生效
 *        ■ 自动配置了一些非常底层的组件。RequestMappingHandlerMapping、这些组件依赖的组件都是从容器中获取
 *        ■ public class DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport
 *   ○ 4、WebMvcAutoConfiguration 里面的配置要能生效 必须  @ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
 *   ○ 5、@EnableWebMvc  导致了 WebMvcAutoConfiguration  没有生效。
 */
@Configuration
//@EnableWebMvc
public class AdminWebConfig implements WebMvcConfigurer {

    /**
     * Filter、Interceptor 几乎拥有相同的功能，都是拦截，用哪个？
     * 1、Filter是Servlet定义的原生组件。好处：脱离Spring应用也能使用
     * 1、Interceptor是Spring定义的接口。只能在Spring应用。可以使用Spring的自动装配等功能
     */
    @Autowired
    RedisUrlCountInterceptor redisUrlCountInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                //拦截请求  /**-- 默认拦截所有请求，包括静态资源
                .addPathPatterns("/**")
                //放行哪些请求
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**", "/aa/**", "/redis/**");
        //如果new RedisUrlCountInterceptor,则RedisUrlCountInterceptor中@Autowired失效，需要从容器中直接拿--注入
        registry.addInterceptor(redisUrlCountInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/login","/css/**","/fonts/**","/images/**","/js/**", "/aa/**", "/redis/**");
    }

    /**
     * 定义静态资源行为
     * @param registry
     */
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 访问 /aa/** 所有请求都去 classpath:/static/ 下面进行匹配
        registry.addResourceHandler("/aa/**")
                .addResourceLocations("classpath:/static/**");
    }*/
}
