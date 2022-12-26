package com.niulijie.jdk8.controller;

import com.niulijie.jdk8.dto.AnnotationPassword;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author niuli
 */
@RestController
public class TestAnnotatinController {

    @PostMapping("/test")
    public String testAnnotationPassword(@Valid @RequestBody AnnotationPassword annotationPassword){
        return "666";
    }

    @PostMapping("/test1")
    public String testAnnotationPassword1(@Validated(value = {AnnotationPassword.UpdateGroup.class}) @RequestBody AnnotationPassword annotationPassword){
        return "888";
    }
}
