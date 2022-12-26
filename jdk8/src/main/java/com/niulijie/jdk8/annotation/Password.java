package com.niulijie.jdk8.annotation;

import com.niulijie.jdk8.validate.PasswordValidate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 测试元注解@Constraint
 * 1.添加@Constraint元注解，则groups和payload方法必写
 * 2.
 * @author niuli
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PasswordValidate.class})
public @interface Password {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "密码为空或不合法";

    /**
     * 这个主要是来进行分组验证的
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * payload 指定的payload，会在验证结果中携带此字段，比如：可以用于验证结果的严重等级分类
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
