package com.niulijie.easyexcel.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 字典信息表
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "uc_attr_dict")
public class UcAttrDict {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private Integer id;

    /**
     * 字典属性ID
     */
    @TableField(value = "attr_id")
    private String attrId;

    /**
     * 字典属性值
     */
    @TableField(value = "attr_value")
    private String attrValue;

    /**
     * 字段状态,0启用  2 删除
     */
    @TableField(value = "`status`")
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

}