package com.atguigu.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/redis")
public class RedisTestController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @GetMapping("/test")
    public void testDataSource(){
        ValueOperations valueOperations = redisTemplate.opsForValue();

        valueOperations.set("hello", "world");
        Object hello = valueOperations.get("hello");
        log.info("hello:"+hello);
        System.out.println(redisConnectionFactory.getClass());
    }

}
