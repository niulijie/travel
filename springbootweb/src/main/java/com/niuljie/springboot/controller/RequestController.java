package com.niuljie.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {

    /**
     * Servlet API：ServletRequestMethodArgumentResolver 处理以下的部分参数
     * WebRequest、ServletRequest、MultipartRequest、 HttpSession、javax.servlet.http.PushBuilder、Principal、InputStream、Reader、HttpMethod、Locale、TimeZone、ZoneId
     * @param request
     * @return
     */
    @GetMapping("/goto")
    public String goToPage(HttpServletRequest request){
        request.setAttribute("msg", "成功了。。。。");
        request.setAttribute("code", 200);
        //转发到/success请求
        return "forward:/success";
    }

    /**
     * Map、Model类型的参数，会返回 mavContainer.getModel() -- ModelAndViewContainer#getModel() --> BindingAwareModelMap 是Model 也是Map
     * @param map
     * @param model
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/params")
    public String testParam(Map<String, Object> map, Model model, HttpServletRequest request, HttpServletResponse response){
        map.put("hello", "world666");
        model.addAttribute("world", "hello555");

        Cookie cookie = new Cookie("c1","v1");
        cookie.setDomain("localhost");
        response.addCookie(cookie);
        request.setAttribute("msg", "成功了。。。。");
        request.setAttribute("code", 200);
        //转发到/success请求
        return "forward:/success";
    }

    /**
     * @RequestAttribute 获取请求域内容
     * @return
     */
    @ResponseBody
    @GetMapping("/success")
    public Map<String,Object> success(@RequestAttribute(value = "msg", required = false) String msg,
                          @RequestAttribute(value = "code", required = false) Integer code,
                          HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        Object msg1 = request.getAttribute("msg");
        Object hello = request.getAttribute("hello");
        Object world = request.getAttribute("world");
        map.put("msg1",msg1);
        map.put("annotation_msg", msg);
        map.put("hello", hello);
        map.put("world", world);
        return map;
    }
}
