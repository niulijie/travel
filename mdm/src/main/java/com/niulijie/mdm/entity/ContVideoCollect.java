package com.niulijie.mdm.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 收藏信息表
 * </p>
 *
 * @author 
 * @since 2022-06-08
 */
@Data
@TableName("cont_video_collect")
public class ContVideoCollect implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 短视频ID
     */
    private Integer videoId;

    /**
     * 发布者ID
     */
    private Integer publisher;

    /**
     * 收藏者ID
     */
    private Integer collector;

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
