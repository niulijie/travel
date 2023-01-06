package com.niulijie.ucenter.pojo.present.VO.account;

import lombok.Data;

@Data
public class AcAccountDeptVO {
    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门层级
     */
    private String deptLevel;

    /**
     * 部门全路径
     */
    private String deptPath;
}
