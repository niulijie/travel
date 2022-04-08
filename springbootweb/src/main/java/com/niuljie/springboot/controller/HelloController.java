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
     *                      #--> getHandler (org.springframework.web.servlet.DispatcherServlet#getHandler) 找到当前请求使用哪个Handler(Controller的方法)处理
     *                          --> RequestMappingHandlerMapping：保存了所有@RequestMapping 和handler的映射规则。(5种)
     *                              --> RequestMappingHandlerAdapter 处理器适配器(4种)
     *                                  --> 执行目标方法 HandlerAdapter#handle(HttpServletRequest, HttpServletResponse, Handler)
     *                                      进入RequestMappingHandlerAdapter#handleInternal,执行目标方法--RequestMappingHandlerAdapter#invokeHandlerMethod,返回ModelAndView
     *                                      ① HandlerMethodArgumentResolver参数解析器，确定将要执行的目标方法的每一个参数的值是什么--26个;
     *                                        判断当前解析器是否支持解析这种参数，支持就调用 resolveArgument
     *                                      ② HandlerMethodReturnValueHandler返回值处理器--15个
     *                                      ③ 将所有26个参数解析器和15个返回值处理器都封装到invocableMethod方法中，ServletInvocableHandlerMethod#invokeAndHandle --> invokeForRequest真正执行目标方法
     *                                          Ⅰ.InvocableHandlerMethod#getMethodArgumentValues 确定目标方法每一个参数得值
     *                                          Ⅱ.HandlerMethodArgumentResolverComposite#resolveArgument
     *                                              HandlerMethodArgumentResolverComposite#getArgumentResolver遍历26个参数解析器，判断哪个支持解析这个参数，并且放入缓存
     *                                              HandlerMethodArgumentResolver#resolveArgument 从requestAttribute中拿出参数值
     *                       #--> org.springframework.web.servlet.DispatcherServlet#processDispatchResult 处理派发结果
     *                          --> org.springframework.web.servlet.DispatcherServlet#render 页面渲染
     *                              --> view = resolveViewName(viewName, mv.getModelInternal(), locale, request) -- DispatcherServlet#resolveViewName
     *                                  viewResolver.resolveViewName(viewName, locale)遍历4种视图解析器根据视图名进行视图解析
     *                              --> view.render(mv.getModelInternal(), request, response) -- View#render 渲染页面
     *                                  ① AbstractView#createMergedOutputModel(model, request, response) 将model值放进LinkedHashMap中
     *                                  ② prepareResponse(request, response); 准备响应
     *                                  ③ InternalResourceView#renderMergedOutputModel(mergedModel, getRequestToExpose(request), response); 渲染准备输入的的model值
     *                                      Ⅰ.exposeModelAsRequestAttributes(model, request); 暴露model数据作为请求域属性 -- 遍历直接放入request中
     *                                      Ⅱ.exposeHelpers(request);
     *                                      Ⅲ.String dispatcherPath = prepareForRendering(request, response);
     *                                      Ⅳ.RequestDispatcher rd = getRequestDispatcher(request, dispatcherPath);
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
