package com.niulijie.springboot.controller;

import com.niulijie.common.dto.BaseResp;
import com.niulijie.common.dto.ResultUtil;
import com.niulijie.springboot.entity.UserTest;
import com.niulijie.springboot.enums.AgeEnum;
import com.niulijie.springboot.enums.GenderEnum;
import com.niulijie.springboot.enums.GradeEnum;
import com.niulijie.springboot.service.UserTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author niulijie
 * @since 2021-12-21 16:56:35
 */
@RestController
@RequestMapping("userTest")
public class UserTestController {

    @Autowired
    private UserTestService userTestService;

    /**
     * @param
     */
    @GetMapping("/insert")
    public BaseResp queryHasTask() {
        UserTest userTest = new UserTest();
        userTest.setName("张三");
        userTest.setAge(AgeEnum.ONE);
        userTest.setSex(GenderEnum.FEMALE);
        userTest.setGrade(GradeEnum.PRIMARY);
        boolean save = userTestService.save(userTest);
        System.out.println("====="+save);
        return ResultUtil.ok(userTest);
    }

}
