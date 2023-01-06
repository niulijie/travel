package com.niulijie.ucenter.pojo.present.VO.account;

import lombok.Data;

import java.util.Date;

@Data
public class AcAccountRoleVO {
    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 关联者ID
     */
    private Integer creator;
    /**
     * 关联者名称
     */
    private String creatorName;
    /**
     * 关联时间
     */
    private Date createdTime;
}
