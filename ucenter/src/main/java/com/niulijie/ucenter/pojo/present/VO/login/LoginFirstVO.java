package com.niulijie.ucenter.pojo.present.VO.login;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.niulijie.ucenter.pojo.view.TenantView;
import lombok.Data;

import java.util.List;

/**
 * @author dcs
 * @description 首次登录返回信息
 * @createTime 2021.8.25
 */
@Data
public class LoginFirstVO {

    /** 是否为超级管理员 */
    @JsonProperty("admin")
    private Boolean admin = false;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("access_id")
    private Long accountId;

    @JsonProperty("user_name")
    private String accountName;

    @JsonProperty("uc_name")
    private String userName;

    @JsonProperty("tenants")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<TenantView> tenants;

    @JsonProperty("tenant_id")
    private Integer TenantId;

    @JsonProperty("dept_ids")
    private List<Integer> DeptIds;

    //@JsonIgnore
    //private List<Integer> tenantIds;

}
