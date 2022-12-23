package com.niulijie.jdk8.annotation;

import java.lang.annotation.*;

/**
 * 可重复使用注解在方法上
 * @author niuli
 */
//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = Persons.class)
public @interface Person {
    String role() default "";
}
