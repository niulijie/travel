package com.niulijie.jdk8.dto;

import com.niulijie.jdk8.annotation.Name;

/**
 * 测试可重复使用注解
 * @author niuli
 */
public class AnnotationNameOnMethod {

    @Name(name = "hello")
    @Name(name = "world")
    public void test(String str1, String str2){
        System.out.println(str1 + "," + str2);
    }
}
