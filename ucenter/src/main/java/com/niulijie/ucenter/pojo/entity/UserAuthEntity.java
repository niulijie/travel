package com.niulijie.ucenter.pojo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.niulijie.ucenter.pojo.present.VO.login.LoginFirstVO;
import com.niulijie.ucenter.pojo.view.TenantView;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 *token生成的参数信息
 */
@Data
public class UserAuthEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户访问ID
     */
    @JsonProperty("access_id")
    private Integer accessId;

    /**
     * 用户ID
     */
    @JsonProperty("user_id")
    private Integer userId;

    /**
     * 部门ID
     */
    @JsonProperty("dept_ids")
    private List<Integer> deptIds;

    /**
     * 租户ID/企业ID
     */
    @JsonProperty("tenant_id")
    private Integer tenantId;

    /**
     * 所属租户ID/企业ID清单
     */
    @JsonProperty("tenant_ids")
    private List<Integer> tenantIds;

    /**
     * 用户账号
     */
    private String username;

    /**
     * 用户账号
     */
    @JsonProperty("sys_code")
    private String sysCode;

    private Boolean admin;

    //首次登录信息初始化
    public UserAuthEntity(LoginFirstVO loginFirstVO) {
        this.accessId = loginFirstVO.getAccountId().intValue();
        this.userId = loginFirstVO.getUserId().intValue();
        this.deptIds = loginFirstVO.getDeptIds();
        this.tenantIds = loginFirstVO.getTenants().stream().map(TenantView::getTenantId).collect(Collectors.toList());
        this.tenantId = tenantIds == null || CollectionUtils.isEmpty(tenantIds)
                ? null
                : tenantIds.get(0);
        this.username = loginFirstVO.getUserName();
        this.admin = loginFirstVO.getAdmin();
    }
}
