package com.atguigu.admin.servlet;

import com.atguigu.admin.config.MyFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * 1、MyServlet --> /my
 * 2、DispatchServlet --> /
 * -----------------------------------------------------------------------------------------------------------------
 * 1、扩展：DispatchServlet 如何注册进来
 *      ● 容器中自动配置了  DispatcherServlet  属性绑定到 WebMvcProperties；对应的配置文件配置项是 spring.mvc。
 *      ● 通过 ServletRegistrationBean<DispatcherServlet> 把 DispatcherServlet  配置进来。
 *      ● 默认映射的是 / 路径。
 * 2、Tomcat-Servlet；
 *      多个Servlet都能处理到同一层路径，精确优选原则
 * ------------------------------------------------------------------------------------------------------------------
 * 1、嵌入式Servlet容器原理
 *   ○ SpringBoot应用启动发现当前是Web应用。web场景包-导入tomcat
 *   ○ web应用会创建一个web版的ioc容器 ServletWebServerApplicationContext
 *   ○ ServletWebServerApplicationContext  启动的时候寻找 ServletWebServerFactory（Servlet 的web服务器工厂---> Servlet 的web服务器）
 *   ○ SpringBoot底层默认有很多的WebServer工厂；TomcatServletWebServerFactory, JettyServletWebServerFactory, or UndertowServletWebServerFactory
 *   ○ 底层直接会有一个自动配置类。ServletWebServerFactoryAutoConfiguration
 *   ○ ServletWebServerFactoryAutoConfiguration导入了ServletWebServerFactoryConfiguration（配置类）
 *   ○ ServletWebServerFactoryConfiguration 配置类 根据动态判断系统中到底导入了那个Web服务器的包。（默认是web-starter导入tomcat包），容器中就有 TomcatServletWebServerFactory
 *   ○ TomcatServletWebServerFactory 创建出Tomcat服务器并启动；TomcatWebServer 的构造器拥有初始化方法initialize---this.tomcat.start();
 *   ○ 内嵌服务器，就是手动把启动服务器的代码调用（tomcat核心jar包存在）
 * 2、定制Servlet容器
 *      ● 实现  WebServerFactoryCustomizer<ConfigurableServletWebServerFactory>
 *          ○ 把配置文件的值和ServletWebServerFactory 进行绑定
 *      ● 修改配置文件 server.xxx
 *      ● 直接自定义 ConfigurableServletWebServerFactory
 */
//保证依赖的组件始终是单实例的
@Configuration(proxyBeanMethods = true)
public class MyRegistConfig {

    @Bean
    public ServletRegistrationBean myServlet(){
        MyServlet myServlet = new MyServlet();
        return new ServletRegistrationBean(myServlet,"/my","/my02");
    }

    @Bean
    public FilterRegistrationBean myFilter(){
        MyFilter myFilter = new MyFilter();
        //return new FilterRegistrationBean(myFilter, myServlet());
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(myFilter);
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/my","/css/*"));
        return filterRegistrationBean;
    }

    @Bean
    public ServletListenerRegistrationBean myListener(){
        MyServletContextListener myServletContextListener = new MyServletContextListener();
        return new ServletListenerRegistrationBean(myServletContextListener);
    }
}
