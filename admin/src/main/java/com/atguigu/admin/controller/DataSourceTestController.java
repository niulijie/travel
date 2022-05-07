package com.atguigu.admin.controller;

import com.atguigu.admin.bean.DaPrLocationDict;
import com.atguigu.admin.bean.UserTest;
import com.atguigu.admin.service.DaPrLocationDictService;
import com.atguigu.admin.service.UserTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;

@Slf4j
@RestController
public class DataSourceTestController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Autowired
    UserTestService userTestService;

    @Autowired
    DaPrLocationDictService daPrLocationDictService;

    @GetMapping("/test/datasource")
    public void testDataSource(){
        Integer integer = jdbcTemplate.queryForObject("select count(*) from user_test", Integer.class);
        log.info("记录总数：{}",  integer);
        log.info("数据源类型：{}", dataSource.getClass());
    }

    @GetMapping("/getById")
    public UserTest getById(@RequestParam Long userId){
        return userTestService.getUserTest(userId);
    }

    @GetMapping("/getDaPrLocationDict")
    public DaPrLocationDict getDaPrLocationDict(@RequestParam Integer id){
        return daPrLocationDictService.getDaPrLocationDict(id);
    }

    @PostMapping("/insert")
    public UserTest insert(@RequestBody UserTest userTest){
        userTestService.saveUser(userTest);
        return userTest;
    }
}
