package com.niuljie.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {

    @GetMapping("/goto")
    public String goToPage(HttpServletRequest request){
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
    public Map<String,Object> success(@RequestAttribute("msg") String msg,
                          @RequestAttribute("code") Integer code,
                          HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        Object msg1 = request.getAttribute("msg");
        map.put("msg1",msg1);
        map.put("annotation_msg", msg);

        return map;
    }
}
