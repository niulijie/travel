package com.niulijie.springboot.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.niulijie.common.dto.BaseResp;
import com.niulijie.common.dto.ResultUtil;
import com.niulijie.springboot.entity.UserTest;
import com.niulijie.springboot.enums.AgeEnum;
import com.niulijie.springboot.enums.GenderEnum;
import com.niulijie.springboot.enums.GradeEnum;
import com.niulijie.springboot.mapper.UserTestMapper;
import com.niulijie.springboot.service.UserTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author niulijie
 * @since 2021-12-21 16:56:35
 */
@RestController
@RequestMapping("userTest")
public class UserTestController {

    @Autowired
    private UserTestService userTestService;

    @Autowired
    private UserTestMapper userTestMapper;
    /**
     * @param
     */
    @GetMapping("/insert")
    public BaseResp<?> insert() {
        UserTest userTest = new UserTest();
        userTest.setName("张三");
        userTest.setAge(AgeEnum.ONE);
        userTest.setSex(GenderEnum.FEMALE);
        userTest.setGrade(GradeEnum.PRIMARY);
        int insert = userTestMapper.insert(userTest);
        System.out.println("====="+insert);
        return ResultUtil.ok(userTest);
    }

    /**
     * 批量插入，如果之前已有数据，则根据已有数据id类型进行递增，否则从1开始递增；但是creatTime和updateTime不会自动填充
     * 若数据库主键字段未设置递增属性，则只会插入一条数据且主键为0
     * @return
     */
    @GetMapping("/batchInsert")
    public BaseResp<?> batchInsert() {
        List<UserTest> ids = userTestService.batch();
        return ResultUtil.ok(ids);
    }


    @GetMapping("/query")
    public BaseResp<?> query() {
        List<UserTest> userTestList = userTestMapper.selectList(Wrappers.<UserTest>lambdaQuery());
        return ResultUtil.ok(userTestList);
    }
}
