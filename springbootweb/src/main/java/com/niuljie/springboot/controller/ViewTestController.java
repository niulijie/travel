package com.niuljie.springboot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewTestController {

    @GetMapping("/atguigu")
    public String atguigu(Model model){

        //model中得数据会被放在请求域中
        model.addAttribute("msg", "你好 牛大");
        model.addAttribute("link", "https://www.baidu.com");
        return "success";
    }
}
