package com.niulijie.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.niulijie.springboot.entity.UserTest;
import com.niulijie.springboot.mapper.UserTestMapper;
import com.niulijie.springboot.service.UserTestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (UserTest)表服务实现类
 *
 * @author niulijie
 * @since 2021-12-21 16:56:32
 */
@Service
public class UserTestServiceImpl extends ServiceImpl<UserTestMapper,UserTest> implements UserTestService {

    @Resource
    private UserTestMapper userTestMapper;


}
