package com.niuljie.springboot.controller;

import com.niuljie.springboot.dto.Human;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloHumanController {

    @Autowired
    private Human human;

    @GetMapping("/hello")
    public String getHello(){
        return human.getClass().toString();
    }

    @GetMapping("/human")
    @ResponseBody
    public Human getHuman(){
        return human;
    }
}
