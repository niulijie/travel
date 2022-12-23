package com.niulijie.jdk8.dto;

import com.niulijie.jdk8.annotation.Person;

/**
 * 测试可重复使用注解在类上
 * @author niuli
 */
@Person(role = "father")
@Person(role = "son")
@Person(role = "husband")
public class AnnotationPersonOnClass {
    String name = "张三";
}
