package com.niulijie.springboot.controller;

import com.niulijie.springboot.util.SpringContextUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author edz
 */
@RestController
@RequestMapping("/application")
public class TestApplicationContrller {

    @RequestMapping("/test1")
    public Object testSpringUtil1() {
        return SpringContextUtils.getBean("testDemo");
    }

}
