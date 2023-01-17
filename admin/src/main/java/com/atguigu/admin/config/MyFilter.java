package com.atguigu.admin.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 1./* -- Servlet写法；/** --> Spring写法
 * 2.配置Filter交给Spring管理
 *      方式1 使用@Component+@Order 注解方式配置简单，支持自定义 Filter 顺序。缺点是只能拦截所有URL，不能通过配置去拦截指定的 URL。
 *          在TestFilter 类上加@Compoent和@Order(1)注解，即可把当前的Filter交给Spring管理。
 *          当有多个Filter时，这里的@Order(1)注解会指定执行顺序，数字越小，越优先执行，如果只写@Order，默认顺序值是Integer.MAX_VALUE
 *      方式2 使用@WebFilter+@ServletComponentScan @ServletComponentScan 可加在当前Filter类上，也可加在启动类上。
 */
@Slf4j
//@WebFilter(urlPatterns = {"/css/*", "/images/*"}) 方式二
public class MyFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("MyFilter初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("MyFilter工作");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.info("MyFilter销毁");
    }
}
