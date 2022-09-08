package com.niulijie.mdm.dto.param;

import java.lang.annotation.*;

/**
 * 锁自定义注解
 * @author 
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {

    /**
     * 锁key
     * @return
     */
    String key() default "";

}
