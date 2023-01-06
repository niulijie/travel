package com.niulijie.ucenter.pojo.present.AO.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author dcs
 * @description 首次登录信息
 * @createTime 2021.8.25
 */
@Data
public class LoginFistAO {
    //账号名
    @NotBlank(message = "账号名不能为空", groups = Login.class)
    @JsonProperty("user_name")
    private String accountName;

    //密码
    @NotBlank(message = "密码不能为空", groups = Login.class)
    @JsonProperty("user_pwd")
    private String password;

    //授权码
    @NotBlank(message = "第三方授权码为空或不合法", groups = Access.class)
    @JsonProperty("access_code")
    private String accessCode;

    //一级模块URL
    @JsonProperty("url")
    private String moduleUrl;

    //系统编号
    @JsonProperty("sys_code")
    private String sysCode;

    public interface Login {}

    public interface Access {}

    public interface Qr {}
}
