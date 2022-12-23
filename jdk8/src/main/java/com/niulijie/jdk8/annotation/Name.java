package com.niulijie.jdk8.annotation;

import java.lang.annotation.*;

/**
 * 可重复使用注解在方法上
 * 1.结果显示，test方法上的注解长度为 1 , 且打印信息为@Names注解，它的值包含了使用的两个注解。因此可知在jdk8中，相同注解只是以集合的方式进行了保存，原理并没有变化
 * 2.@Repeatable 所声明的注解，其元注解@Target的使用范围要比@Repeatable的值声明的注解中的@Target的范围要大或相同，否则编译器错误，
 * 显示@Repeatable值所声明的注解的元注解@Target不是@Repeatable声明的注解的@Target的子集
 * 3.@Repeatable注解声明的注解的元注解@Retention的周期要比@Repeatable的值指向的注解的@Retention得周期要小或相同
 * 周期长度为 SOURCE(源码) < CLASS (字节码) < RUNTIME(运行)
 * 4.Repeatable注解声明的注解的元注解@Target和@Retention可以省略
 * @author niuli
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Name.Names.class) //Names 中方法名必须为value 和 @Repeatable中参数名一致
public @interface Name {
    String name() default "value";

    //@Target({ElementType.METHOD, ElementType.TYPE}) @Repeatable 所声明的注解，其元注解@Target的使用范围要比@Repeatable的值声明的注解中的@Target的范围要大或相同，否则编译器错误，
    @Target({ElementType.METHOD})
    //@Retention(RetentionPolicy.CLASS) @Repeatable注解声明的注解的元注解@Retention的周期要比@Repeatable的值指向的注解的@Retention得周期要小或相同
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Names {
        Name[] value();
    }
}
