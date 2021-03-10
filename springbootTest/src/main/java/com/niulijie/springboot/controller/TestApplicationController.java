package com.niulijie.springboot.controller;

import com.niulijie.springboot.util.SpringContextUtils;
import com.niulijie.springboot.util.SpringContextUtils2;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author edz
 */
@RestController
@RequestMapping("/application")
public class TestApplicationController {

    @RequestMapping("/test1")
    public Object testSpringUtil1() {
        return SpringContextUtils.getBean("testDemo");
    }

    @RequestMapping("/test2")
    public Object testSpringUtil2() {
        return SpringContextUtils2.getBean("testDemo");
    }
}
