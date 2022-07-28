package com.niulijie.mdm.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * 参数验证配置
 * @author dufa
 * @date 2020-11-29
 */
@Configuration
public class MethodValidationConfig {

    /**
     * 全局参数异常参数配置（@PathVariable和@RequestParam）
     * 解决@RequestParam 参数验证全局异常失效
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        MethodValidationPostProcessor postProcessor = new MethodValidationPostProcessor();
        postProcessor.setValidator(validator());
        return postProcessor;
    }

    /**
     * 多参数验证，快速失败
     */
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();

        return validatorFactory.getValidator();
    }
}
