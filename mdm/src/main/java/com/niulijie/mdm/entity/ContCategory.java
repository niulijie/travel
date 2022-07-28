package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 视频栏目表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("cont_category")
public class ContCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，分类id
     */
    @TableId(value = "cate_id", type = IdType.AUTO)
    private Integer cateId;

    /**
     * 专栏名称
     */
    private String cateName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 添加人员id
     */
    private Integer userId;

    /**
     * 删除标识 1正常 2 删除
     */
    private Integer deleted;

    /**
     * 添加时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
