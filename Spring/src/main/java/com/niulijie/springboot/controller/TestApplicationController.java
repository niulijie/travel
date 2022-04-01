package com.niulijie.springboot.controller;

import com.niulijie.springboot.entity.Car;
import com.niulijie.springboot.entity.Person;
import com.niulijie.springboot.util.SpringContextUtils;
import com.niulijie.springboot.util.SpringContextUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @RestController相当于@Controller和@ResponseBody合体
 * @author edz
 */
@RestController
@RequestMapping("/application")
public class TestApplicationController {

    @Autowired
    Car car;

    @Autowired
    Person person;

    @RequestMapping("/test1")
    public Object testSpringUtil1() {
        return SpringContextUtils.getBean("testDemo");
    }

    @RequestMapping("/test2")
    public Object testSpringUtil2() {
        return SpringContextUtils2.getBean("testDemo");
    }

    @RequestMapping("/car")
    public Car car(){
        return car;
    }

    @RequestMapping("/person")
    public Person person(){
        return person;
    }
}
