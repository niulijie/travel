package com.atguigu.admin.service;

import com.atguigu.admin.bean.UserTest;
import com.atguigu.admin.mapper.UserTestMapper;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserTestService {

    @Autowired
    UserTestMapper userTestMapper;

    Counter counter;

    public UserTestService(MeterRegistry meterRegistry){
        counter = meterRegistry.counter("userTestService.getUserTest.count");
    }

    public UserTest getUserTest(Long userId){
        counter.increment();
        return userTestMapper.getUserTest(userId);
    }

    public UserTest saveUser(UserTest userTest) {
        userTestMapper.insert(userTest);
        return userTest;
    }
}
