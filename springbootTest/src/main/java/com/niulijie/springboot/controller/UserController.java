package com.niulijie.springboot.controller;

import com.niulijie.springboot.test.MainScanConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

//    @GetMapping("/test")
//    public void test(){
//        AnnotationConfigApplicationContext applicationContext2 = new AnnotationConfigApplicationContext(MainScanConfig.class);
//        String[] definitionNames = applicationContext2.getBeanDefinitionNames();
//        for (String name : definitionNames) {
//            System.out.println(name);
//        }
//    }
}
