package com.niulijie.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.niulijie.springboot.entity.UserTest;

import java.util.List;

/**
 * (UserTest)表服务实现类
 *
 * @author niulijie
 * @since 2021-12-21 16:56:31
 */
public interface UserTestService extends IService<UserTest> {
    /**
     * 用Stream循环批量插入数据，mybatis批量插入瓶颈是1000，所以需要分批插入
     * @return
     */
    List<UserTest> batch();
}
