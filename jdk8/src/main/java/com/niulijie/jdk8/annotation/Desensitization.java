package com.niulijie.jdk8.annotation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.niulijie.jdk8.enums.DesensitizationTypeEnum;
import com.niulijie.jdk8.config.DesensitizationSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于脱敏的注解
 * @author niuli
 */
@Target(ElementType.FIELD) //可用在字段上
@Retention(RetentionPolicy.RUNTIME) //运行时生效
@JacksonAnnotationsInside //元注解，主要是用户打包其他注解一起使用。
@JsonSerialize(using = DesensitizationSerialize.class) // 可自定义序列化可以用在注解上，方法上，字段上，类上，运行时生效等等，根据提供的序列化类里面的重写方法实现自定义序列化
public @interface Desensitization {
    /**
     * 脱敏数据类型，在MY_RULE的时候，startInclude和endExclude生效
     */
    DesensitizationTypeEnum type() default DesensitizationTypeEnum.MY_RULE;

    /**
     * 脱敏开始位置（包含）
     */
    int startInclude() default 0;

    /**
     * 脱敏结束位置（不包含）
     */
    int endExclude() default 0;
}
