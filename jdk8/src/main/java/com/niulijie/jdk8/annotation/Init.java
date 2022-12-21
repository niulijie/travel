package com.niulijie.jdk8.annotation;

import java.lang.annotation.*;

/**
 * 元注解：注解其他注解
 * @Target 描述注解的使用范围(即：被描述的注解可以用在什么地方)
 * @Retention 需要在什么级别保存该注解信息，用户描述注解的生命周期 RetentionPolicy.SOURCE(源码)<RetentionPolicy.CLASS(字节码)<RetentionPolicy.RUNTIME(运行时)
 * @Documented 说明该注解将被包含在javadoc中
 * @Inherited 说明子类可以继承父类中的该注解
 * 1.其中的每一个方法实际上是声明了一个配置参数 注解的参数：参数类型+参数名()
 * 2.方法的名称就是参数的名称
 * 3.返回值类型就是参数的类型(返回值只能是基本类型Class，String，enum)
 * 4.可以通过default来声明参数的默认值
 * 5.如果只有一个参数成员，一般参数名为value
 * 6.注解元素必须要有值，我们定义注解元素时，经常使用空字符串、0作为默认值
 * @author niuli
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Init {
    // 注解的参数：参数类型+参数名()
    String name() default "";
    int age() default 0;
    int id() default -1; // 如果默认值为-1，代表不存在

    String[] schools() default {"清华", "北大"};
}
