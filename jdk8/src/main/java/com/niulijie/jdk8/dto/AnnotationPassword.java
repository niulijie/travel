package com.niulijie.jdk8.dto;

import com.niulijie.jdk8.annotation.Password;
import lombok.Data;

/**
 * @author niuli
 */
@Data
public class AnnotationPassword {

    public interface AddGroup {}

    public interface UpdateGroup {}

    @Password(min = 6, max = 30, message = "请输入6-30位数字与字母相结合的新密码", groups = {AddGroup.class})
    private String newPwd;
}
