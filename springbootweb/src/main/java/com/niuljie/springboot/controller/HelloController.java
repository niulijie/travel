package com.niuljie.springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    /**
     * 1.只要静态资源放在类路径下： /static or /public or /resources or /META-INF/resources
     * 访问 ： 当前项目根路径/ + 静态资源名
     * 原理： 静态映射/**
     * 2.请求进来，先去找Controller看能不能处理。不能处理的所有请求又都交给静态资源处理器。静态资源也找不到则响应404页面
     *
     * 3.请求 --> doGet/doPost (org.springframework.web.servlet.FrameworkServlet#doGet)
     *          --> processRequest (org.springframework.web.servlet.FrameworkServlet#processRequest)
     *              --> doService (org.springframework.web.servlet.DispatcherServlet#doService)
     *                  --> doDispatch (org.springframework.web.servlet.DispatcherServlet#doDispatch)
     *                      --> getHandler (org.springframework.web.servlet.DispatcherServlet#getHandler) 找到当前请求使用哪个Handler(Controller的方法)处理
     *                          --> RequestMappingHandlerMapping：保存了所有@RequestMapping 和handler的映射规则。
     * 4.SpringBoot自动配置欢迎页的 WelcomePageHandlerMapping --> 访问 /能访问到index.htm;
     *   SpringBoot自动配置了默认的 RequestMappingHandlerMapping
     * @return
     */
    //@RequestMapping("/bug.png")
    @GetMapping("/bug.png")
    public String testSpringUtil1() {
        return "aaaaaa";
    }

}
