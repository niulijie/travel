package com.niulijie.easyexcel.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 人员字段字典表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "uc_user_attr")
public class UcUserAttr {

    /**
     * 属性ID
     */
    @TableId(value = "attr_id", type = IdType.ASSIGN_UUID)
    private String attrId;

    /**
     * 所属企业ID
     */
    @TableField(value = "tenant_id")
    private Integer tenantId;

    /**
     * 属性字段名
     */
    @TableField(value = "attr_field")
    private String attrField;

    /**
     * 属性名称
     */
    @TableField(value = "attr_name")
    private String attrName;

    /**
     * 属性类型，0默认属性，1.单行文本 2.多行文本 3.手机号 4.邮箱 5.超链接 6.数字 7.日期 8.时间 9.下拉选择 10.多项选择 11.开关
     */
    @TableField(value = "attr_type")
    private Integer attrType;

    /**
     * 是否允许为空，0允许，1不允许
     */
    @TableField(value = "is_null")
    private Integer isNull;

    /**
     * 是否是默认属性，0是，1不是，默认是0
     */
    @TableField(value = "is_default")
    private Integer isDefault;

    /**
     * 是否列表展示 0是，1不是
     */
    @TableField(value = "is_list_show")
    private Integer isListShow;

    /**
     * 字段的显示顺序
     */
    @TableField(value = "attr_order")
    private Integer attrOrder;

    /**
     * 添加者信息
     */
    @TableField(value = "creator")
    private Integer creator;

    /**
     * 状态 0 启用 2 删除
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

}
