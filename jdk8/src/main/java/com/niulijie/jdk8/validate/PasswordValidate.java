package com.niulijie.jdk8.validate;

import com.niulijie.jdk8.annotation.Password;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 这个自定义注解逻辑处理类由于实现了ConstraintValidator接口，所以它默认被spring管理成bean,
 * 所以可以在这个逻辑处理类里面用@Autowiredu或者@Resources注入别的服务，而且不用在类上面用@Compent注解成spring的bean.
 * @author niuli
 */
public class PasswordValidate implements ConstraintValidator<Password, String> {

    /**
     * 是否校验
     */
    @Value("${common.check: true}")
    private boolean check;

    int min;

    int max;

    /**
     * 初始化 加载注解的信息。
     * @param constraintAnnotation
     */
    @Override
    public void initialize(Password constraintAnnotation) {
        min = constraintAnnotation.min();
        max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 校验未开启--直接返回成功
        if (!check) {
            return true;
        }

        if(StringUtils.isEmpty(s)){
            return false;
        }

        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{" + min + "," + max + "}$";
        if (!Pattern.matches(regex, s)){
            return false;
        }
        return true;
    }
}
