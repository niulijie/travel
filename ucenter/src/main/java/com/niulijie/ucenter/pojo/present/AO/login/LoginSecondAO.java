package com.niulijie.ucenter.pojo.present.AO.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginSecondAO {
    //账号ID
    @NotBlank(message = "账号ID不能为空", groups = LoginFistAO.Login.class)
    @JsonProperty("access_id")
    private Integer accountId;

    //一级模块URL
    @JsonProperty("url")
    private String moduleUrl;
}
