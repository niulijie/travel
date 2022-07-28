package com.niulijie.mdm.dto.param;

import com.niulijie.mdm.dto.enums.ActionTypeEnum;
import lombok.Data;

/**
 * @author niuli
 */
@Data
public class Dept {

    /**
     * 部门ID
     */
    private Integer deptId;

    /**
     * 上级部门ID
     */
    private Integer parentId;

    /**
     * 部门姓名
     */
    private String deptName;

    /** 部门等级 */
    private String deptLevel;

    /** 部门全路径 */
    private String deptPath;

    /** 部门状态 */
    private int status;

    /**
     * 数据变更类型 {@link ActionTypeEnum}
     */
    private Integer actionType;

}
