package com.niulijie.easyexcel.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: sharing-backstage
 * @Description: 用户实体类
 * @Author: zwx
 * @Date: 2022/5/12 14:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    /**
     * 姓名
     */
    private String name;
    /**
     * 工号
     */
    private String workCode;
    /**
     * 年龄
     */
    private String age;
    /**
     * 下拉框
     */
    private String select;
    /**
     * 性别
     */
    private String sex;
}

