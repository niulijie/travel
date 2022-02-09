package com.niulijie.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.niulijie.springboot.entity.UserTest;
import com.niulijie.springboot.enums.AgeEnum;
import com.niulijie.springboot.enums.GenderEnum;
import com.niulijie.springboot.enums.GradeEnum;
import com.niulijie.springboot.mapper.UserTestMapper;
import com.niulijie.springboot.service.UserTestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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


    /**
     * 用Stream循环批量插入数据，mybatis批量插入瓶颈是1000，所以需要分批插入
     *
     * @return
     */
    @Override
    public List<UserTest> batch() {
        List<UserTest> userTestList = Lists.newArrayList();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 5000; i++) {
            UserTest userTest = new UserTest();
            userTest.setName("张三"+i);
            int bought=(int)(Math.random()*3);
            if(bought==1){
                userTest.setGrade(GradeEnum.PRIMARY);
                userTest.setAge(AgeEnum.ONE);
                userTest.setSex(GenderEnum.FEMALE);
            }else if(bought==2){
                userTest.setGrade(GradeEnum.SECONDARY);
                userTest.setAge(AgeEnum.TWO);
                userTest.setSex(GenderEnum.MALE);
            }else{
                userTest.setGrade(GradeEnum.HIGH);
                userTest.setAge(AgeEnum.THREE);
                userTest.setSex(GenderEnum.FEMALE);
            }
            //creatTime和updateTime不会自动填充
            userTest.setCreateTime(now);
            userTest.setUpdateTime(now);
            userTestList.add(userTest);
        }
        int count = 1000;
        int dateCount = userTestList.size();
        int cycle = (dateCount % count == 0) ? (dateCount / count) : (dateCount / count + 1);
        //分批次处理，循环次数, 跳过数据
        /**
         * 数值流 IntStream, DoubleStream, LongStream
         * 数值流转换为流
         *  Stream<Integer> stream = intStream.boxed();
         *  这两个方法的区别在于一个是闭区间，一个是半开半闭区间
         *  rangeClosed(1, 100) ：[1, 100]
         *  range(1, 100) ：[1, 100)
         */
        IntStream.rangeClosed(1, cycle).map(i -> (i - 1) * count).mapToObj(skip -> userTestList.stream().skip(skip)
                .limit(count).collect(Collectors.toList())).forEach(reportList -> {
            userTestMapper.insertList(reportList);
        });
        return userTestList;
    }
}
