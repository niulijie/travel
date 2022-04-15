package com.niuljie.springboot.controller;

import com.niuljie.springboot.dto.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Controller
public class ResponseTestController {

    /**
     * 1.HandlerMethodReturnValueHandlerComposite#handleReturnValue 处理返回值
     *   1.1  HandlerMethodReturnValueHandlerComposite#selectHandler  通过返回值和返回类型寻找对应的返回值处理器
     *   1.2  返回值处理器调用handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest); 处理返回值
     * 2.RequestResponseBodyMethodProcessor#handleReturnValue 可以处理标注的@ResponseBody返回值
     *   2.1 AbstractMessageConverterMethodProcessor#writeWithMessageConverters 利用messageConverter处理进行处理 将数据写为json
     *      2.1.1 内容协商
     *      2.1.2 AbstractMessageConverterMethodProcessor#getAcceptableMediaTypes 获取浏览器可接收内容类型
     *            ContentNegotiationManager#resolveMediaTypes 通过内容协商管理器来获取可接收类型
     *              --> HeaderContentNegotiationStrategy 默认使用基于请求头的协商策略
     *                  --> HeaderContentNegotiationStrategy#resolveMediaTypes 确定客户端可以接收的内容类型
     *      2.1.3 AbstractMessageConverterMethodProcessor#getProducibleMediaTypes 服务器最终根据自己自身的能力，决定服务器能生产出什么样内容类型的数据
     *      2.1.4 SpringMVC会挨个遍历所有容器底层的消息转换器 HttpMessageConverter（8个） ，看谁能处理
     *              WebMvcConfigurationSupport#addDefaultHttpMessageConverters --> 系统默认底层配置Converter
     *              AbstractJackson2HttpMessageConverter#writeInternal  写对象内容
     *              最终 MappingJackson2HttpMessageConverter  把对象转为JSON（利用底层的jackson的objectMapper转换的）
     *              利用MappingJackson2HttpMessageConverter将对象转为json再写出去。
     *  ---------------------------------------------------------------------------------------------------------------------------------
     *  1. 浏览器发请求直接返回xml  【application/xml】  jacksonXmlConverter
     *  2. 如果是ajax请求 返回json 【application/json】  jacksonJsonConverter
     *  3. 如果是硅谷app发请求，返回自定义协议数据 【application/x-guigu】  xxxxConverter
     *          属性值1;属性值2;
     *  步骤：
     *  1. 添加自定义的MessageConverter到系统底层
     *  2. 系统底层会统计所有MessageConverter能操作哪些类型
     *  3. 客户端内容协商[guigu-->guigu]
     * @return
     */
    @ResponseBody
    @GetMapping("/test/person")
    public Person getPerson(){
        Person person = new Person();
        person.setAge(18);
        person.setBirth(new Date());
        person.setUserName("zhangsan");
        return person;
    }
}
