package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
    * 部门表
    */
@Data
@TableName(value = "uc_dept")
public class UcDept {
    /**
     * 主键，部门id
     */
    @TableId(value = "dept_id", type = IdType.INPUT)
    private Integer deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 上级部门ID
     */
    private Integer parentId;

    /**
     * 部门层级
     */
    private String deptLevel;

    /**
     * 部门全路径
     */
    private String deptPath;

    /**
     * 部门排序
     */
    private Integer deptOrder;

    /**
     * 部门状态 0 启用 1停用 2删除
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}