package com.niulijie.ucenter.pojo.present.VO.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class LoginSecondVO extends LoginFirstVO {
    @JsonProperty("tenant_id")
    private Integer tenantId;

    @JsonProperty("tenant_name")
    private String tenantName;

    @JsonProperty("module")
    private List<ModuleVO> moduleItems = new ArrayList<>();
}
