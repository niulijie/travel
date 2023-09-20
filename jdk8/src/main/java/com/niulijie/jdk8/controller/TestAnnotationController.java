package com.niulijie.jdk8.controller;

import com.niulijie.jdk8.dto.AnnotationPassword;
import com.niulijie.jdk8.dto.UserDesensitization;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author niuli
 */
@RestController
public class TestAnnotationController {

    @PostMapping("/test")
    public String testAnnotationPassword(@Valid @RequestBody AnnotationPassword annotationPassword){
        return "666";
    }

    @PostMapping("/test1")
    public String testAnnotationPassword1(@Validated(value = {AnnotationPassword.UpdateGroup.class}) @RequestBody AnnotationPassword annotationPassword){
        return "888";
    }

    @PostMapping("/user")
    public UserDesensitization testUser(){
        UserDesensitization userDesensitization = new UserDesensitization();
        userDesensitization.setUserName("牛大大");
        userDesensitization.setAddress("地球中国-北京市通州区京东总部2号楼");
        userDesensitization.setPhone("13729387633");
        userDesensitization.setPassword("dxyt122343@=-");
        System.out.println(userDesensitization);
        return userDesensitization;
    }
}
