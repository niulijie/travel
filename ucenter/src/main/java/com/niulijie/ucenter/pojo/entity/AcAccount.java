package com.niulijie.ucenter.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 账号表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "ac_account")
public class AcAccount {
    /**
     * 账号ID
     */
    @TableId(value = "account_id", type = IdType.AUTO)
    private Integer accountId;

    /**
     * 账号名
     */
    @TableField(value = "account_name")
    private String accountName;

    /**
     * 账号类型：0-默认，1-自定义
     */
    @TableField(value = "account_type")
    private Integer accountType;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 创建者ID
     */
    @TableField(value = "creator",fill = FieldFill.INSERT)
    private Integer creator;

    /**
     * 账号状态: 0-启用, 1-停用, 2-删除
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 账号密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 密码盐
     */
    @TableField(value = "`salt`")
    private String salt;

    /**
     * 记录时间
     */
    @TableField(value = "created_time")
    private Date createdTime;

    private Boolean admin;
    /**
     * 修改时间
     */
    //@TableField(value = "updated_time")
    //private Date updatedTime;
}
