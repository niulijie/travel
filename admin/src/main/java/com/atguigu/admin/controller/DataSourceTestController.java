package com.atguigu.admin.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@Slf4j
@RestController
public class DataSourceTestController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @GetMapping("/test/datasource")
    public void testDataSource(){
        Integer integer = jdbcTemplate.queryForObject("select count(*) from device_info", Integer.class);
        log.info("记录总数：{}",  integer);

        log.info("数据源类型：{}", dataSource.getClass());
    }
}
