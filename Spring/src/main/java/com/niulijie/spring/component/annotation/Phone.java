package com.niulijie.spring.component.annotation;

/**
 * @author zhoupengbing
 * @packageName com.telecomyt.ucenter.annotation
 * @email zhoupengbing@telecomyt.com.cn
 * @description 自定义注解BindUserDeviceParam
 * @createTime 2019年12月10日 09:39:00 @Version v1.0
 */

import org.hibernate.validator.constraints.CompositionType;
import org.hibernate.validator.constraints.ConstraintComposition;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@ConstraintComposition(CompositionType.OR)
@Pattern(regexp = "^1[3-9]\\d{9}$")
@Documented
@Constraint(validatedBy = {})
@Target({METHOD, FIELD, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface Phone {

  String message() default "";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
