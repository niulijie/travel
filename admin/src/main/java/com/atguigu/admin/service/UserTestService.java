package com.atguigu.admin.service;

import com.atguigu.admin.bean.UserTest;
import com.atguigu.admin.mapper.UserTestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTestService {

    @Autowired
    UserTestMapper userTestMapper;

    public UserTest getUserTest(Long userId){
        return userTestMapper.getUserTest(userId);
    }

    public UserTest saveUser(UserTest userTest) {
        userTestMapper.insert(userTest);
        return userTest;
    }
}
